package org.prgrms.wumo.domain.member.dto.response;

public record MemberTokenResponse(
		String grantType,
		String accessToken,
		String refreshToken
) {
}
