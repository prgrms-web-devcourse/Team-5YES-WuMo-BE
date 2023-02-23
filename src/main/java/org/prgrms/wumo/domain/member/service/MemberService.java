package org.prgrms.wumo.domain.member.service;

import org.prgrms.wumo.domain.member.dto.request.MemberEmailCheckRequest;
import org.prgrms.wumo.domain.member.model.Email;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.global.exception.custom.DuplicateException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public void checkEmail(MemberEmailCheckRequest memberEmailCheckRequest) {
		if (checkEmailDuplicate(memberEmailCheckRequest.email())) {
			throw new DuplicateException("이메일이 중복됩니다.");
		}
	}

	private boolean checkEmailDuplicate(Email email) {
		return memberRepository.existsByEmail(email);
	}
}
