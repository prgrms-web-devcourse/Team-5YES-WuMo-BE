package org.prgrms.wumo.global.jwt;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.prgrms.wumo.global.exception.ExceptionResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper;

	@Override
	public void handle(
		HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
		throws IOException {

		log.info("No Authorities", accessDeniedException);
		ExceptionResponse exceptionResponse = new ExceptionResponse("없는 리소스입니다.");

		String responseBody = objectMapper.writeValueAsString(exceptionResponse);
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(responseBody);
	}
}
