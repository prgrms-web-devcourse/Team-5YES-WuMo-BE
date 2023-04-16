package org.prgrms.wumo.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "회원 로그인 응답 정보")
public record MemberLoginResponse(
	@Schema(description = "인증 타입", example = "Bearer", requiredMode = Schema.RequiredMode.REQUIRED)
	String grantType,

	@Schema(description = "액세스토큰", example = "토큰값", requiredMode = Schema.RequiredMode.REQUIRED)
	String accessToken
) {
}
