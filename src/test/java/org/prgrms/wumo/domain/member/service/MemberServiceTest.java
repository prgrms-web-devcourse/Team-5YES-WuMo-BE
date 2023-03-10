package org.prgrms.wumo.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Collections;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.wumo.domain.member.dto.request.MemberEmailCheckRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberLoginRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberNicknameCheckRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberRegisterRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberUpdateRequest;
import org.prgrms.wumo.domain.member.dto.response.MemberGetResponse;
import org.prgrms.wumo.domain.member.dto.response.MemberLoginResponse;
import org.prgrms.wumo.domain.member.dto.response.MemberRegisterResponse;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.global.exception.ExceptionMessage;
import org.prgrms.wumo.global.exception.custom.DuplicateException;
import org.prgrms.wumo.global.jwt.JwtTokenProvider;
import org.prgrms.wumo.global.jwt.WumoJwt;
import org.prgrms.wumo.global.repository.KeyValueRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService??? ")
public class MemberServiceTest {

	@InjectMocks
	MemberService memberService;

	@Mock
	MemberRepository memberRepository;

	@Mock
	JwtTokenProvider jwtTokenProvider;

	@Mock
	KeyValueRepository keyValueRepository;

	@Mock
	ApplicationEventPublisher applicationEventPublisher;

	@BeforeEach
	void setUp() {
		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(1L, null, Collections.EMPTY_LIST);

		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Nested
	@DisplayName("registerMember ???????????? ???????????? ?????? ??? ")
	class RegisterMember {
		//given
		String email = "5yes@gmail.com";
		String nickname = "?????????";
		String password = "qwe12345";

		MemberRegisterRequest memberRegisterRequest
				= new MemberRegisterRequest(email, nickname, password);

		Member member = getMemberData(email, nickname, password);

		@Test
		@DisplayName("???????????? ???????????? ???????????? ????????? ????????????")
		void success() {
			//mocking
			given(memberRepository.save(any(Member.class)))
					.willReturn(member);

			//when
			MemberRegisterResponse result = memberService.registerMember(memberRegisterRequest);

			//then
			assertThat(result.id()).isEqualTo(1L);
			then(memberRepository)
					.should()
					.save(any(Member.class));
		}
	}

	@Nested
	@DisplayName("checkEmail ???????????? ????????? ???????????? ?????? ??? ")
	class CheckEmail {
		//given
		String email = "5yes@gmail.com";

		MemberEmailCheckRequest memberEmailCheckRequest
				= new MemberEmailCheckRequest(email);

		@Test
		@DisplayName("?????? ????????? ?????????????????? ????????? ????????????")
		void fail_with_exist_emil() {
			//mocking
			given(memberRepository.existsByEmail(anyString()))
					.willReturn(true);

			//when, then
			assertThatThrownBy(() -> memberService.checkEmail(memberEmailCheckRequest.email()))
					.isInstanceOf(DuplicateException.class)
					.hasMessage("???????????? ???????????????.");
		}
	}

	@Nested
	@DisplayName("checkNickname ???????????? ????????? ???????????? ?????? ??? ")
	class CheckNickname {
		//given
		String nickname = "?????????";

		MemberNicknameCheckRequest memberNicknameCheckRequest
				= new MemberNicknameCheckRequest(nickname);

		@Test
		@DisplayName("?????? ???????????? ?????????????????? ????????? ????????????")
		void fail_with_exist_emil() {
			//mocking
			given(memberRepository.existsByNickname(anyString()))
					.willReturn(true);

			//when, then
			assertThatThrownBy(() -> memberService.checkNickname(memberNicknameCheckRequest.nickname()))
					.isInstanceOf(DuplicateException.class)
					.hasMessage("???????????? ???????????????.");
		}
	}

	@Nested
	@DisplayName("login ???????????? ????????? ?????? ??? ")
	class LoginMember {
		//given
		String email = "5yes@gmail.com";
		String nickname = "?????????????????????";
		String password = "qwe12345";

		MemberLoginRequest memberLoginRequest
				= new MemberLoginRequest(email, password);

		Member member = getMemberData(email, nickname, password);

		WumoJwt wumoJwt = WumoJwt.builder()
				.grantType("grant type")
				.accessToken("access token")
				.refreshToken("refresh token")
				.build();

		@Test
		@DisplayName("???????????? jwt token??? ????????????")
		void success() {
			//mocking
			given(memberRepository.findByEmail(anyString()))
					.willReturn(Optional.of(member));
			given(jwtTokenProvider.generateToken(anyString()))
					.willReturn(wumoJwt);

			//when
			MemberLoginResponse result = memberService.loginMember(memberLoginRequest);

			//then
			assertThat(result).usingRecursiveComparison().isEqualTo(wumoJwt);
			then(memberRepository)
					.should()
					.findByEmail(anyString());
			then(jwtTokenProvider)
					.should()
					.generateToken(anyString());
		}

		@Test
		@DisplayName("???????????? ???????????? ????????? ????????? ????????????")
		void fail_not_match_email() {
			//mocking
			given(memberRepository.findByEmail(anyString()))
					.willReturn(Optional.ofNullable(null));

			//when, then
			assertThatThrownBy(() -> memberService.loginMember(memberLoginRequest))
					.isInstanceOf(EntityNotFoundException.class)
					.hasMessage(String.format(ExceptionMessage.ENTITY_NOT_FOUND.name(), ExceptionMessage.MEMBER.name()));
		}

		@Test
		@DisplayName("??????????????? ???????????? ????????? ????????? ????????????")
		void fail_not_match_password() {
			//mocking
			String wrongPassword = "asd12345";
			MemberLoginRequest wrongMemberLoginRequest
					= new MemberLoginRequest(email, wrongPassword);

			given(memberRepository.findByEmail(anyString()))
					.willReturn(Optional.of(member));

			//when, then
			assertThatThrownBy(() -> memberService.loginMember(wrongMemberLoginRequest))
					.isInstanceOf(BadCredentialsException.class)
					.hasMessage("??????????????? ???????????? ????????????.");
		}
	}

	@Nested
	@DisplayName("getMember ???????????? ?????? ?????? ?????? ?????? ??? ")
	class GetMember {
		//given
		long memberId = 1L;
		String email = "5yes@gmail.com";
		String nickname = "?????????????????????";
		String password = "qwe12345";

		Member member = getMemberData(email, nickname, password);

		@Test
		@DisplayName("??? ????????? ????????????")
		void success() {
			//mocking
			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.of(member));

			//when
			MemberGetResponse result = memberService.getMember();

			//then
			assertThat(result.nickname()).isEqualTo(member.getNickname());
			then(memberRepository)
					.should()
					.findById(anyLong());
		}
	}

	@Nested
	@DisplayName("updateMember ???????????? ?????? ?????? ?????? ?????? ??? ")
	class UpdateMember {
		//given
		String email = "5yes@gmail.com";
		String nicknameBefore = "?????????????????????";
		String password = "qwe12345";

		String nicknameAfter = "???????????????";

		MemberUpdateRequest memberUpdateRequest
				= new MemberUpdateRequest(1L, nicknameAfter, null);

		Member memberBefore = getMemberData(email, nicknameBefore, password);
		Member memberAfter = getMemberData(email, nicknameAfter, password);

		@Test
		@DisplayName("?????? ??? ????????? ?????? ????????? ????????????")
		void success() {
			//mocking
			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.of(memberBefore));
			given(memberRepository.save(any(Member.class)))
					.willReturn(memberAfter);

			//when
			MemberGetResponse result = memberService.updateMember(memberUpdateRequest);

			//then
			assertThat(result.nickname()).isEqualTo(nicknameAfter);
			then(memberRepository)
					.should()
					.findById(anyLong());
			then(memberRepository)
					.should()
					.save(any(Member.class));
		}
	}

	private Member getMemberData(String email, String nickname, String password) {
		return Member.builder()
				.id(1L)
				.email(email)
				.nickname(nickname)
				.password(password)
				.build();
	}
}
