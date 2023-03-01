package org.prgrms.wumo.global.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.prgrms.wumo.global.exception.ExceptionResponse;
import org.prgrms.wumo.global.exception.custom.TokenException;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author taehee
 * 토큰 관련 예외 처리
 * JwtTokenProvider에서 발생한 예외 처리
 */
@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		try {
			filterChain.doFilter(request, response);
		} catch (TokenException tokenException) {

			ExceptionResponse exceptionResponse = new ExceptionResponse(tokenException.getMessage());

			String responseBody = objectMapper.writeValueAsString(exceptionResponse);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(responseBody);
		}
	}
}
