package org.prgrms.wumo.global.oauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "oauth 로그인 응답 정보")
public record OauthLoginResponse(
		@Schema(description = "인증 타입", example = "Bearer", requiredMode = Schema.RequiredMode.REQUIRED)
		String grantType,

		@Schema(description = "액세스토큰", example = "토큰값", requiredMode = Schema.RequiredMode.REQUIRED)
		String accessToken,

		@Schema(description = "리프레시토큰", example = "토큰값", requiredMode = Schema.RequiredMode.REQUIRED)
		String refreshToken
) {
}
