package org.prgrms.wumo.global.config;

import static org.springframework.http.HttpMethod.OPTIONS;

import org.prgrms.wumo.global.config.properties.JwtProperties;
import org.prgrms.wumo.global.jwt.CustomAccessDeniedHandler;
import org.prgrms.wumo.global.jwt.CustomAuthenticationEntryPoint;
import org.prgrms.wumo.global.jwt.JwtAuthenticationFilter;
import org.prgrms.wumo.global.jwt.JwtExceptionFilter;
import org.prgrms.wumo.global.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtProperties jwtProperties;
	private final ObjectMapper objectMapper;

	private final String[] apiUrls = {
		"/docs", "/docs/index.html", "/docs/**", "/index.html", "/swagger-ui.html"
	};

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
			.antMatchers(apiUrls)
			.antMatchers("/api/v1/members/signup")
			.antMatchers("/api/v1/members/login")
			.antMatchers("/api/v1/members/send-code")
			.antMatchers("/api/v1/members/check-code");
	}

	@Bean
	public JwtTokenProvider jwtTokenProvider() {
		return new JwtTokenProvider(jwtProperties.getIssuer(), jwtProperties.getSecretKey(),
			jwtProperties.getAccessTokenExpireSeconds(), jwtProperties.getRefreshTokenExpireSeconds());
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
			.formLogin().disable()
			.rememberMe().disable()
			.logout().disable()

			.cors()
			.and()

			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()

			.authorizeHttpRequests()
			.antMatchers(OPTIONS, "/api/*").permitAll()
			.antMatchers(apiUrls).permitAll()
			.antMatchers(
				"/api/v1/members/signup",
				"/api/v1/members/login",
				"/api/v1/members/send-code",
				"/api/v1/members/check-code").permitAll()
			.and()

			.exceptionHandling()
			.authenticationEntryPoint(customAuthenticationEntryPoint())
			.accessDeniedHandler(customAccessDeniedHandler())
			.and()

			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider()),
				UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new JwtExceptionFilter(objectMapper), JwtAuthenticationFilter.class);

		return http.build();
	}
}
