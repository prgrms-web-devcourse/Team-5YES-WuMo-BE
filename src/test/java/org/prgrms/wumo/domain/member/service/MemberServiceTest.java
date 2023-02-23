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
import org.prgrms.wumo.domain.member.model.Email;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.global.exception.custom.DuplicateException;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service layer를 통해 ")
public class MemberServiceTest {

	@InjectMocks
	MemberService memberService;

	@Mock
	MemberRepository memberRepository;

	@Nested
	@DisplayName("이메일 중복체크 요청 시 ")
	class RegisterMember {

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
}
