package org.prgrms.wumo.domain.member.dto.request;

import org.prgrms.wumo.domain.member.model.Email;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "이메일 중복체크 요청 정보")
public record MemberEmailCheckRequest(
	@Schema(description = "중복 체크할 회원 이메일", example = "5yes@gmail.com", required = true)
	Email email
) {
}
