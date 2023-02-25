package org.prgrms.wumo.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.wumo.domain.member.dto.request.MemberEmailCheckRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberNicknameCheckRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberRegisterRequest;
import org.prgrms.wumo.domain.member.dto.response.MemberRegisterResponse;
import org.prgrms.wumo.domain.member.model.Email;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.global.exception.custom.DuplicateException;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService의 ")
public class MemberServiceTest {

	@InjectMocks
	MemberService memberService;

	@Mock
	MemberRepository memberRepository;

	@Nested
	@DisplayName("registerMember 메소드는 회원가입 요청 시 ")
	class RegisterMember {

		//given
		String email = "5yes@gmail.com";
		String nickname = "오예스오리지널";
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
		void with_exist_emil() {
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
		void with_exist_emil() {
			//mocking
			given(memberRepository.existsByNickname(nickname))
				.willReturn(true);

			//when, then
			assertThatThrownBy(() -> memberService.checkNickname(memberNicknameCheckRequest.nickname()))
				.isInstanceOf(DuplicateException.class)
				.hasMessage("닉네임이 중복됩니다.");
		}
	}
}
