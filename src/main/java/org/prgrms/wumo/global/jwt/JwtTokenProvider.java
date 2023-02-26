package org.prgrms.wumo.global.jwt;

import java.util.Collections;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	private static final String BEARER_TYPE = "Bearer";

	private final String issuer;
	private final String key;
	private final long ACCESS_TOKEN_EXPIRE_SECONDS;
	private final long REFRESH_TOKEN_EXPIRE_SECONDS;

	private final SecretKey secretKey;

	public JwtTokenProvider(
		@Value("${jwt.issuer}") String issuer,
		@Value("${jwt.secret-key}") String key,
		@Value("${jwt.access-token-expire-seconds}") long accessSeconds,
		@Value("${jwt.refresh-token-expire-seconds}") long refreshSeconds) {
		this.issuer = issuer;
		this.key = key;
		this.ACCESS_TOKEN_EXPIRE_SECONDS = accessSeconds;
		this.REFRESH_TOKEN_EXPIRE_SECONDS = refreshSeconds;
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
	}

	public WumoJwt generateToken(String memberId) {
		Date currentDate = new Date();

		return WumoJwt.builder()
			.grantType(BEARER_TYPE)
			.accessToken(generateAccessToken(memberId, currentDate))
			.refreshToken(generateRefreshToken(currentDate))
			.build();
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

	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);
		return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", Collections.emptyList());
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException exception) {
			log.info("Invalid JWT Token", exception);
		} catch (ExpiredJwtException exception) {
			log.info("Expired JWT Token", exception);
		} catch (UnsupportedJwtException exception) {
			log.info("Unsupported JWT Token", exception);
		} catch (IllegalArgumentException exception) {
			log.info("JWT claims string is empty.", exception);
		}
		return false;
	}

	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(accessToken)
				.getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
}
