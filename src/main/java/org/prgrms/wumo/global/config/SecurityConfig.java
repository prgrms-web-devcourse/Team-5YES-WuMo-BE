package org.prgrms.wumo.global.config;

import org.prgrms.wumo.global.jwt.JwtAuthenticationFilter;
import org.prgrms.wumo.global.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtConfig jwtConfig;

	@Bean
	public JwtTokenProvider jwtTokenProvider() {
		return new JwtTokenProvider(jwtConfig.getIssuer(), jwtConfig.getSecretKey(),
			jwtConfig.getAccessTokenExpireSeconds(), jwtConfig.getRefreshTokenExpireSeconds());
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.httpBasic().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeHttpRequests()
			.antMatchers("/api/v1/members/signup", "/api/v1/members/login", "/swagger-ui.html")
			.permitAll()
			.and()
			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider()),
				UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
