package org.prgrms.wumo.domain.member.dto.request;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "이메일 중복체크 요청 정보")
public record MemberEmailCheckRequest(
	@NotBlank(message = "이메일은 필수 입력사항입니다.")
	@Schema(description = "중복 체크할 회원 이메일", example = "5yes@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
	String email
) {
}
