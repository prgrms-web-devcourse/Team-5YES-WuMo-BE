package org.prgrms.wumo.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "회원가입 응답 정보")
public record MemberRegisterResponse(
	@Schema(description = "회원가입된 회원 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	Long id
) {
}
