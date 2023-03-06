package org.prgrms.wumo.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	private String issuer;
	private String secretKey;
	private long accessTokenExpireSeconds;
	private long refreshTokenExpireSeconds;
}
