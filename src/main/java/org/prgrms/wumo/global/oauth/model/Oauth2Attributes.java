package org.prgrms.wumo.global.oauth.model;

import java.util.Map;

/**
 * provider로부터 오는 다른 JSON 형식들을 관리하기 위한 클래스
 * @param sub
 * @param provider
 * @param email
 * @param attributes
 */
public record Oauth2Attributes(
		String sub,
		SocialOauthType provider,
		String email,
		Map<String, Object> attributes
) {

	public static Oauth2Attributes of(SocialOauthType provider, Map<String, Object> attributes) {
		switch (provider) {
			case GOOGLE -> {
				return ofGoogle(provider, attributes);
			}
			default -> throw new RuntimeException("알 수 없는 소셜 로그인 형식입니다.");
		}
	}

	public Map<String, Object> convertToMap() {
		return Map.of(
				"sub", this.sub,
				"provider", this.provider,
				"email", this.email,
				"id", this.attributes
		);
	}

	private static Oauth2Attributes ofGoogle(SocialOauthType provider, Map<String, Object> attributes) {
		String sub = (String)attributes.get("sub");
		String email = (String)attributes.get("email");
		return new Oauth2Attributes(sub, provider, email, attributes);
	}
}
