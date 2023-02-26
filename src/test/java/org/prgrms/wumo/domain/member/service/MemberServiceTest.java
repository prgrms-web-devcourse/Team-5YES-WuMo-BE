package org.prgrms.wumo.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

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
import org.prgrms.wumo.domain.member.dto.response.MemberLoginResponse;
import org.prgrms.wumo.domain.member.dto.response.MemberRegisterResponse;
import org.prgrms.wumo.domain.member.model.Email;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.global.exception.custom.DuplicateException;
import org.prgrms.wumo.global.jwt.JwtTokenProvider;
import org.prgrms.wumo.global.jwt.WumoJwt;
import org.springframework.security.authentication.BadCredentialsException;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService의 ")
public class MemberServiceTest {

	@InjectMocks
	MemberService memberService;

	@Mock
	MemberRepository memberRepository;

	@Mock
	JwtTokenProvider jwtTokenProvider;

	@Nested
	@DisplayName("registerMember 메소드는 회원가입 요청 시 ")
	class RegisterMember {
		//given
		String email = "5yes@gmail.com";
		String nickname = "오예스";
		String password = "qwe12345";

		MemberRegisterRequest memberRegisterRequest
			= new MemberRegisterRequest(email, nickname, password);

		Member member = Member.builder()
			.id(1L)
			.email(email)
			.nickname(nickname)
			.password(password)
			.build();

		@Test
		@DisplayName("이메일과 닉네임이 중복되지 않으면 성공한다")
		void success() {
			//mocking
			given(memberRepository.save(any(Member.class)))
				.willReturn(member);

			//when
			MemberRegisterResponse result = memberService.registerMember(memberRegisterRequest);

			//then
			assertThat(result.id()).isEqualTo(1L);
			verify(memberRepository, times(1)).save(any(Member.class));
		}
	}

	@Nested
	@DisplayName("checkEmail 메소드는 이메일 중복체크 요청 시 ")
	class CheckEmail {
		//given
		String email = "5yes@gmail.com";

		MemberEmailCheckRequest memberEmailCheckRequest
			= new MemberEmailCheckRequest(email);

		@Test
		@DisplayName("이미 가입된 이메일이라면 예외가 발생한다")
		void fail_with_exist_emil() {
			//mocking
			given(memberRepository.existsByEmail(any(Email.class)))
				.willReturn(true);

			//when, then
			assertThatThrownBy(() -> memberService.checkEmail(memberEmailCheckRequest.email()))
				.isInstanceOf(DuplicateException.class)
				.hasMessage("이메일이 중복됩니다.");
		}
	}

	@Nested
	@DisplayName("checkNickname 메소드는 닉네임 중복체크 요청 시 ")
	class CheckNickname {
		//given
		String nickname = "오예스";

		MemberNicknameCheckRequest memberNicknameCheckRequest
			= new MemberNicknameCheckRequest(nickname);

		@Test
		@DisplayName("이미 사용중인 닉네임이라면 예외가 발생한다")
		void fail_with_exist_emil() {
			//mocking
			given(memberRepository.existsByNickname(any(String.class)))
				.willReturn(true);

			//when, then
			assertThatThrownBy(() -> memberService.checkNickname(memberNicknameCheckRequest.nickname()))
				.isInstanceOf(DuplicateException.class)
				.hasMessage("닉네임이 중복됩니다.");
		}
	}

	@Nested
	@DisplayName("login 메소드는 로그인 요청 시 ")
	class Login {
		//given
		String email = "5yes@gmail.com";
		String nickname = "오예스오리지널";
		String password = "qwe12345";

		MemberLoginRequest memberLoginRequest
			= new MemberLoginRequest(email, password);

		Member member = Member.builder()
			.id(1L)
			.email(email)
			.nickname(nickname)
			.password(password)
			.build();

		@Test
		@DisplayName("성공하면 jwt token을 반환한다")
		void success() {
			//mocking
			WumoJwt wumoJwt = WumoJwt.builder()
				.grantType("grant type")
				.accessToken("access token")
				.refreshToken("refresh token")
				.build();

			given(memberRepository.findByEmail(any(Email.class)))
				.willReturn(Optional.of(member));
			given(jwtTokenProvider.generateToken(any(String.class)))
				.willReturn(wumoJwt);

			//when
			MemberLoginResponse result = memberService.login(memberLoginRequest);

			//then
			assertThat(result).usingRecursiveComparison().isEqualTo(wumoJwt);
			verify(memberRepository, times(1)).findByEmail(any(Email.class));
			verify(jwtTokenProvider, times(1)).generateToken(any(String.class));
		}

		@Test
		@DisplayName("이메일이 일치하지 않으면 예외가 발생한다")
		void fail_not_match_email() {
			//mocking
			given(memberRepository.findByEmail(any(Email.class)))
				.willReturn(Optional.ofNullable(null));

			//when, then
			assertThatThrownBy(() -> memberService.login(memberLoginRequest))
				.isInstanceOf(EntityNotFoundException.class)
				.hasMessage("일치하는 회원이 없습니다.");
		}

		@Test
		@DisplayName("비밀번호가 일치하지 않으면 예외가 발생한다")
		void fail_not_match_password() {
			//mocking
			String wrongPassword = "asd12345";
			MemberLoginRequest wrongMemberLoginRequest
				= new MemberLoginRequest(email, wrongPassword);

			given(memberRepository.findByEmail(any(Email.class)))
				.willReturn(Optional.of(member));

			//when, then
			assertThatThrownBy(() -> memberService.login(wrongMemberLoginRequest))
				.isInstanceOf(BadCredentialsException.class)
				.hasMessage("비밀번호가 일치하지 않습니다.");
		}
	}
}
