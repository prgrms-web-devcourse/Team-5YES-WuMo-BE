package org.prgrms.wumo.domain.member.service;

import static org.prgrms.wumo.global.jwt.JwtUtil.getMemberId;
import static org.prgrms.wumo.global.jwt.JwtUtil.isValidAccess;
import static org.prgrms.wumo.global.mapper.MemberMapper.toMember;
import static org.prgrms.wumo.global.mapper.MemberMapper.toMemberGetResponse;
import static org.prgrms.wumo.global.mapper.MemberMapper.toMemberLoginResponse;
import static org.prgrms.wumo.global.mapper.MemberMapper.toMemberRegisterResponse;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.member.dto.request.MemberLoginRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberRegisterRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberReissueRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberUpdateRequest;
import org.prgrms.wumo.domain.member.dto.response.MemberGetResponse;
import org.prgrms.wumo.domain.member.dto.response.MemberLoginResponse;
import org.prgrms.wumo.domain.member.dto.response.MemberRegisterResponse;
import org.prgrms.wumo.domain.member.model.Email;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.global.exception.custom.DuplicateException;
import org.prgrms.wumo.global.exception.custom.InvalidRefreshTokenException;
import org.prgrms.wumo.global.jwt.JwtTokenProvider;
import org.prgrms.wumo.global.jwt.WumoJwt;
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

	@Transactional
	public MemberRegisterResponse registerMember(MemberRegisterRequest memberRegisterRequest) {
		checkEmail(memberRegisterRequest.email());
		checkNickname(memberRegisterRequest.nickname());
		Member member = memberRepository.save(toMember(memberRegisterRequest));
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
	public MemberLoginResponse loginMember(MemberLoginRequest memberLoginRequest) {
		Member member = memberRepository.findByEmail(new Email(memberLoginRequest.email()))
			.orElseThrow(() -> new EntityNotFoundException("일치하는 회원이 없습니다."));

		if (member.isNotValidPassword(memberLoginRequest.password())) {
			throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
		}

		WumoJwt wumoJwt = jwtTokenProvider.generateToken(String.valueOf(member.getId()));
		member.updateRefreshToken(wumoJwt.getRefreshToken());

		return toMemberLoginResponse(wumoJwt);
	}

	public void logoutMember() {
		getMemberEntity(getMemberId()).logout();
		SecurityContextHolder.clearContext();
	}

	public MemberLoginResponse reissueMember(MemberReissueRequest memberReissueRequest) {
		String accessToken = memberReissueRequest.accessToken();
		String refreshToken = memberReissueRequest.refreshToken();
		jwtTokenProvider.validateToken(refreshToken);

		String memberId = jwtTokenProvider.extractMember(accessToken);
		Member member = getMemberEntity(Integer.parseInt(memberId));

		if (!member.getRefreshToken().equals(refreshToken)) {
			throw new InvalidRefreshTokenException("인증 정보가 만료되었습니다.");
		}

		WumoJwt wumoJwt = jwtTokenProvider.generateToken(memberId);
		member.updateRefreshToken(wumoJwt.getRefreshToken());

		return toMemberLoginResponse(wumoJwt);
	}

	@Transactional(readOnly = true)
	public MemberGetResponse getMember(long memberId) {
		validateAccess(memberId);
		return toMemberGetResponse(getMemberEntity(memberId));
	}

	@Transactional
	public MemberGetResponse updateMember(MemberUpdateRequest memberUpdateRequest) {
		long memberId = memberUpdateRequest.id();
		validateAccess(memberId);

		Member member = getMemberEntity(memberId);
		member.update(
			memberUpdateRequest.nickname(),
			memberUpdateRequest.password(),
			memberUpdateRequest.profileImage());
		return toMemberGetResponse(memberRepository.save(member));
	}

	private Member getMemberEntity(long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException("일치하는 회원이 없습니다."));
	}

	private void validateAccess(long memberId) {
		if (!isValidAccess(memberId)) {
			throw new AccessDeniedException("잘못된 접근입니다.");
		}
	}

	private boolean checkEmailDuplicate(String email) {
		return memberRepository.existsByEmail(new Email(email));
	}

	private boolean checkNicknameDuplicate(String nickname) {
		return memberRepository.existsByNickname(nickname);
	}
}
