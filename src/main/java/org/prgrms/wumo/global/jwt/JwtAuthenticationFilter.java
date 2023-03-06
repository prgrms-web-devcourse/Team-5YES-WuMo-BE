package org.prgrms.wumo.global.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER_TYPE = "Bearer";

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		String token = extractToken((HttpServletRequest)request);

		if (StringUtils.hasText(token)) {
			jwtTokenProvider.validateToken(token);
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	private String extractToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
