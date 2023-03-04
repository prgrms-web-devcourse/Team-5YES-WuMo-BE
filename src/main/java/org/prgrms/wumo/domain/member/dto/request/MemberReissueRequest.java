package org.prgrms.wumo.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "회원 토큰 재발급 요청 정보")
public record MemberReissueRequest(
	@Schema(description = "액세스토큰", example = "토큰값", requiredMode = Schema.RequiredMode.REQUIRED)
	String accessToken,

	@Schema(description = "리프레시토큰", example = "토큰값", requiredMode = Schema.RequiredMode.REQUIRED)
	String refreshToken
) {
}
