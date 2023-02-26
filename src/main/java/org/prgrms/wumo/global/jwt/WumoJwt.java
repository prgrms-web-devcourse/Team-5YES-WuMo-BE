package org.prgrms.wumo.global.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WumoJwt {
	private final String grantType;
	private final String accessToken;
	private final String refreshToken;

	@Builder
	public WumoJwt(String grantType, String accessToken, String refreshToken) {
		this.grantType = grantType;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
