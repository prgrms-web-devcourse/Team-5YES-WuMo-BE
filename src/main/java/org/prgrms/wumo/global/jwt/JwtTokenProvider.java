package org.prgrms.wumo.global.jwt;

import java.util.Collections;
import java.util.Date;

import javax.crypto.SecretKey;

import org.prgrms.wumo.global.config.properties.JwtProperties;
import org.prgrms.wumo.global.exception.custom.ExpiredTokenException;
import org.prgrms.wumo.global.exception.custom.InvalidTokenException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	private static final String BEARER_TYPE = "Bearer";

	private final String issuer;
	private final long ACCESS_TOKEN_EXPIRE_SECONDS;
	private final long REFRESH_TOKEN_EXPIRE_SECONDS;

	private final SecretKey secretKey;

	public JwtTokenProvider(JwtProperties jwtProperties) {
		this.issuer = jwtProperties.getIssuer();
		this.ACCESS_TOKEN_EXPIRE_SECONDS = jwtProperties.getAccessTokenExpireSeconds();
		this.REFRESH_TOKEN_EXPIRE_SECONDS = jwtProperties.getRefreshTokenExpireSeconds();
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecretKey()));
	}

	public WumoJwt generateToken(String memberId) {
		Date currentDate = new Date();

		return WumoJwt.builder()
				.grantType(BEARER_TYPE)
				.accessToken(generateAccessToken(memberId, currentDate))
				.refreshToken(generateRefreshToken(currentDate))
				.build();
	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);
		return new UsernamePasswordAuthenticationToken(
				Long.parseLong(claims.getSubject()), "", Collections.emptyList());
	}

	public void validateToken(String accessToken) {
		try {
			Jwts.parserBuilder()
					.setSigningKey(secretKey)
					.build()
					.parseClaimsJws(accessToken);
		} catch (ExpiredJwtException exception) {
			log.info("Expired JWT Token", exception);
			throw new ExpiredTokenException("만료된 토큰입니다.");
		} catch (JwtException | IllegalArgumentException exception) {
			log.info("Invalid JWT Token.", exception);
			throw new InvalidTokenException("올바르지 않은 토큰입니다.");
		}
	}

	public String extractMember(String accessToken) {
		return parseClaims(accessToken).getSubject();
	}

	public long getRefreshTokenExpireSeconds() {
		return REFRESH_TOKEN_EXPIRE_SECONDS;
	}

	private String generateAccessToken(String memberId, Date currentDate) {
		Date expireDate = new Date(currentDate.getTime() + ACCESS_TOKEN_EXPIRE_SECONDS);

		return Jwts.builder()
				.setIssuer(issuer)
				.setSubject(memberId)
				.setIssuedAt(new Date())
				.setExpiration(expireDate)
				.signWith(secretKey)
				.compact();
	}

	private String generateRefreshToken(Date currentDate) {
		Date expireDate = new Date(currentDate.getTime() + REFRESH_TOKEN_EXPIRE_SECONDS);

		return Jwts.builder()
				.setExpiration(expireDate)
				.signWith(secretKey)
				.compact();
	}

	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(secretKey)
					.build()
					.parseClaimsJws(accessToken)
					.getBody();
		} catch (ExpiredJwtException exception) {
			return exception.getClaims();
		}
	}
}
