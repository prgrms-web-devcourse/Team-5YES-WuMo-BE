package org.prgrms.wumo.global.jwt;

import java.util.Collections;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

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
public class JwtTokenProvider {

	private static final String BEARER_TYPE = "Bearer";

	private final String issuer;
	private final long ACCESS_TOKEN_EXPIRE_SECONDS;
	private final long REFRESH_TOKEN_EXPIRE_SECONDS;

	private final SecretKey secretKey;

	public JwtTokenProvider(String issuer, String key, long accessSeconds, long refreshSeconds) {
		this.issuer = issuer;
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
		return new UsernamePasswordAuthenticationToken(
			Long.parseLong(claims.getSubject()), "", Collections.emptyList());
	}

	public boolean validateToken(String accessToken) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(accessToken);
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
		} catch (ExpiredJwtException exception) {
			return exception.getClaims();
		}
	}
}
