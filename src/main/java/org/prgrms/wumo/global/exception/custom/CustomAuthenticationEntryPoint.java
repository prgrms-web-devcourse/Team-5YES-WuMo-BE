package org.prgrms.wumo.global.exception.custom;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.prgrms.wumo.global.exception.ExceptionResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	public void commence(
		HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
		throws IOException {

		log.info("Not Authenticated Request", authException);
		ExceptionResponse exceptionResponse = new ExceptionResponse("로그인 후 사용가능합니다.");

		String responseBody = objectMapper.writeValueAsString(exceptionResponse);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(responseBody);
	}
}
