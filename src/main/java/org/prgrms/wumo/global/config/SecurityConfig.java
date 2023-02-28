package org.prgrms.wumo.global.config;

import org.prgrms.wumo.global.exception.custom.CustomAccessDeniedHandler;
import org.prgrms.wumo.global.exception.custom.CustomAuthenticationEntryPoint;
import org.prgrms.wumo.global.jwt.JwtAuthenticationFilter;
import org.prgrms.wumo.global.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtConfig jwtConfig;
	private final ObjectMapper objectMapper;

	@Bean
	public JwtTokenProvider jwtTokenProvider() {
		return new JwtTokenProvider(jwtConfig.getIssuer(), jwtConfig.getSecretKey(),
			jwtConfig.getAccessTokenExpireSeconds(), jwtConfig.getRefreshTokenExpireSeconds());
	}

	@Bean
	public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint(objectMapper);
	}

	@Bean
	public CustomAccessDeniedHandler customAccessDeniedHandler() {
		return new CustomAccessDeniedHandler(objectMapper);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.httpBasic().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeHttpRequests()
			.antMatchers(
				"/api/v1/members/signup",
				"/api/v1/members/login",
				"/swagger-ui.html",
				"/api/v1/routes")
			.permitAll()
			.and()
			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider()),
				UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling()
			.authenticationEntryPoint(customAuthenticationEntryPoint())
			.accessDeniedHandler(customAccessDeniedHandler());

		return http.build();
	}
}
