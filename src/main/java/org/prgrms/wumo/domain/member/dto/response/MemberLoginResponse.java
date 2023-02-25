package org.prgrms.wumo.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "회원 로그인 응답 정보")
public record MemberLoginResponse(
	@Schema(description = "인증 타입", example = "Bearer", required = true)
	String grantType,

	@Schema(description = "액세스토큰", example = "토큰값", required = true)
	String accessToken,

	@Schema(description = "리프레시토큰", example = "토큰값", required = true)
	String refreshToken
) {
}
