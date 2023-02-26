package org.prgrms.wumo.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
	private String issuer;
	private String secretKey;
	private long accessTokenExpireSeconds;
	private long refreshTokenExpireSeconds;
}
