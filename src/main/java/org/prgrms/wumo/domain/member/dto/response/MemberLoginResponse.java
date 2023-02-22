package org.prgrms.wumo.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "회원 로그인 응답 정보")
public record MemberLoginResponse(
		@Schema(description = "토큰", example = "토큰값(변경가능성, 추가가능성 있음)", required = true)
		String token
) {
}
