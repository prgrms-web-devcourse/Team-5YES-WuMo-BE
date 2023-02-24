package org.prgrms.wumo.domain.member.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import org.prgrms.wumo.domain.member.model.Email;
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
	@DisplayName("checkEmail 메소드는 이메일 중복체크 요청 시 ")
	class CheckEmail {

		//given
		Email email = new Email("5yes@gmail.com");

		MemberEmailCheckRequest memberEmailCheckRequest
			= new MemberEmailCheckRequest(email);

		@Test
		@DisplayName("이미 가입된 이메일이라면 예외가 발생한다")
		void with_exist_emil() {
			//mocking
			given(memberRepository.existsByEmail(memberEmailCheckRequest.email()))
				.willReturn(true);

			//when, then
			assertThatThrownBy(() -> memberService.checkEmail(memberEmailCheckRequest))
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
			assertThatThrownBy(() -> memberService.checkNickname(memberNicknameCheckRequest))
				.isInstanceOf(DuplicateException.class)
				.hasMessage("닉네임이 중복됩니다.");
		}
	}
}
