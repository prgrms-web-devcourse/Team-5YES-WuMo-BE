package org.prgrms.wumo.domain.member.service;

import static org.prgrms.wumo.domain.member.mapper.MemberMapper.toMember;
import static org.prgrms.wumo.domain.member.mapper.MemberMapper.toMemberGetResponse;
import static org.prgrms.wumo.domain.member.mapper.MemberMapper.toMemberRegisterResponse;
import static org.prgrms.wumo.domain.member.mapper.MemberMapper.toMemberTokenResponse;
import static org.prgrms.wumo.domain.member.mapper.MemberMapper.toOauthLoginResponse;
import static org.prgrms.wumo.global.jwt.JwtUtil.getMemberId;
import static org.prgrms.wumo.global.jwt.JwtUtil.isValidAccess;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.member.dto.request.MemberLoginRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberPasswordUpdateRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberRegisterRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberReissueRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberUpdateRequest;
import org.prgrms.wumo.domain.member.dto.response.MemberGetResponse;
import org.prgrms.wumo.domain.member.dto.response.MemberRegisterResponse;
import org.prgrms.wumo.domain.member.dto.response.MemberTokenResponse;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.global.event.MemberCreateEvent;
import org.prgrms.wumo.global.exception.ExceptionMessage;
import org.prgrms.wumo.global.exception.custom.DuplicateException;
import org.prgrms.wumo.global.exception.custom.ExpiredTokenException;
import org.prgrms.wumo.global.exception.custom.InvalidCodeException;
import org.prgrms.wumo.global.jwt.JwtTokenProvider;
import org.prgrms.wumo.global.jwt.JwtUtil;
import org.prgrms.wumo.global.jwt.WumoJwt;
import org.prgrms.wumo.global.oauth.dto.OauthLoginResponse;
import org.prgrms.wumo.global.repository.KeyValueRepository;
import org.prgrms.wumo.global.sender.Sender;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final KeyValueRepository keyValueRepository;
	private final Sender sender;
	private final ApplicationEventPublisher applicationEventPublisher;

	public void sendCode(String toAddress) {
		sender.sendCode(toAddress);
	}

	public void checkCode(String toAddress, String code) {
		if (!keyValueRepository.get(toAddress).equals(code)) {
			throw new InvalidCodeException("유효하지 않은 인증 코드입니다.");
		}
	}

	@Transactional
	public MemberRegisterResponse registerMember(MemberRegisterRequest memberRegisterRequest) {
		String email = memberRegisterRequest.email();
		checkEmail(email);
		checkNickname(memberRegisterRequest.nickname());

		Member member = memberRepository.save(toMember(memberRegisterRequest));
		applicationEventPublisher.publishEvent(new MemberCreateEvent(email));
		return toMemberRegisterResponse(member.getId());
	}

	@Transactional(readOnly = true)
	public void checkEmail(String email) {
		if (checkEmailDuplicate(email)) {
			throw new DuplicateException("이메일이 중복됩니다.");
		}
	}

	@Transactional(readOnly = true)
	public void checkNickname(String nickname) {
		if (checkNicknameDuplicate(nickname)) {
			throw new DuplicateException("닉네임이 중복됩니다.");
		}
	}

	@Transactional
	public MemberTokenResponse loginMember(MemberLoginRequest memberLoginRequest) {
		Member member = getMemberEntityByEmail(memberLoginRequest.email());
		validatePassword(member, memberLoginRequest.password());

		String memberId = String.valueOf(member.getId());
		WumoJwt wumoJwt = getWumoJwt(memberId);

		return toMemberTokenResponse(wumoJwt);
	}

	@Transactional
	public void logoutMember() {
		keyValueRepository.delete(String.valueOf(getMemberId()));
		SecurityContextHolder.clearContext();
	}

	@Transactional
	public MemberTokenResponse reissueMember(MemberReissueRequest memberReissueRequest, String refreshToken) {
		String accessToken = memberReissueRequest.accessToken();
		jwtTokenProvider.validateToken(refreshToken);
		String memberId = jwtTokenProvider.extractMember(accessToken);

		if (!refreshToken.equals(keyValueRepository.get(memberId))) {
			throw new ExpiredTokenException("인증 정보가 만료되었습니다.");
		}

		keyValueRepository.delete(memberId);
		WumoJwt wumoJwt = getWumoJwt(memberId);

		return toMemberTokenResponse(wumoJwt);
	}

	@Transactional(readOnly = true)
	public MemberGetResponse getMember() {
		return toMemberGetResponse(getMemberEntity(JwtUtil.getMemberId()));
	}

	@Transactional
	public MemberGetResponse updateMember(MemberUpdateRequest memberUpdateRequest) {
		long memberId = memberUpdateRequest.id();
		validateAccess(memberId);

		Member member = getMemberEntity(memberId);
		member.update(
				memberUpdateRequest.nickname(),
				memberUpdateRequest.profileImage()
		);
		return toMemberGetResponse(memberRepository.save(member));
	}

	@Transactional
	public void updateMemberPassword(MemberPasswordUpdateRequest memberPasswordUpdateRequest) {
		Member member = getMemberEntity(getMemberId());
		validatePassword(member, memberPasswordUpdateRequest.password());
		member.updatePassword(memberPasswordUpdateRequest.newPassword());
	}

	@Transactional
	public OauthLoginResponse registerOrGet(String email) {
		Member member = memberRepository.findByEmail(email)
				.orElseGet(() -> memberRepository.save(toMember(email)));
		WumoJwt wumoJwt = getWumoJwt(String.valueOf(member.getId()));
		return toOauthLoginResponse(wumoJwt);
	}

	private void validatePassword(Member member, String password) {
		if (member.isNotValidPassword(password)) {
			throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
		}
	}

	private WumoJwt getWumoJwt(String memberId) {
		WumoJwt wumoJwt = jwtTokenProvider.generateToken(memberId);
		keyValueRepository.save(
				memberId,
				wumoJwt.getRefreshToken(),
				jwtTokenProvider.getRefreshTokenExpireSeconds()
		);
		return wumoJwt;
	}

	private void validateAccess(long memberId) {
		if (!isValidAccess(memberId)) {
			throw new AccessDeniedException(ExceptionMessage.WRONG_ACCESS.name());
		}
	}

	private boolean checkEmailDuplicate(String email) {
		return memberRepository.existsByEmail(email);
	}

	private boolean checkNicknameDuplicate(String nickname) {
		return memberRepository.existsByNickname(nickname);
	}

	private Member getMemberEntity(long memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format(ExceptionMessage.ENTITY_NOT_FOUND.name(), ExceptionMessage.MEMBER.name())
				));
	}

	private Member getMemberEntityByEmail(String email) {
		return memberRepository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format(ExceptionMessage.ENTITY_NOT_FOUND.name(), ExceptionMessage.MEMBER.name())
				));
	}
}
