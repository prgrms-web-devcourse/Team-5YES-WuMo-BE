package org.prgrms.wumo.global.config;

import static org.springframework.http.HttpMethod.OPTIONS;

import org.prgrms.wumo.global.jwt.CustomAccessDeniedHandler;
import org.prgrms.wumo.global.jwt.CustomAuthenticationEntryPoint;
import org.prgrms.wumo.global.jwt.JwtAuthenticationFilter;
import org.prgrms.wumo.global.jwt.JwtExceptionFilter;
import org.prgrms.wumo.global.oauth.OauthAuthenticationSuccessHandler;
import org.prgrms.wumo.global.oauth.service.CustomOauthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtExceptionFilter jwtExceptionFilter;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;
	private final CustomOauthService customOauthService;
	private final OauthAuthenticationSuccessHandler oauthAuthenticationSuccessHandler;

	private final String[] apiUrls = {
			"/wumo/**", "/swagger/**", "/swagger-ui.html", "/swagger-ui/**", "/favicon.ico", "/favicon.io"
	};

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
				.antMatchers(apiUrls)
				.antMatchers("/api/v1/members/signup")
				.antMatchers("/api/v1/members/login")
				.antMatchers("/api/v1/members/send-code")
				.antMatchers("/api/v1/members/check-code")
				.antMatchers("/api/v1/members/reissue");
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.httpBasic().disable()
				.formLogin().disable()
				.rememberMe().disable()
				.logout().disable()
				.requestCache().disable()
				.headers().disable()

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
						"/api/v1/members/check-code",
						"/api/v1/members/reissue",
						"/oauth2/**",
						"/login/oauth2/**")
				.permitAll()
				.anyRequest().authenticated()
				.and()

				.oauth2Login()
				.authorizationEndpoint().baseUri("/oauth2/authorization")
				.and()
				.userInfoEndpoint().userService(customOauthService)
				.and()
				.successHandler(oauthAuthenticationSuccessHandler)
				.and()

				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)

				.exceptionHandling()
				.authenticationEntryPoint(customAuthenticationEntryPoint)
				.accessDeniedHandler(customAccessDeniedHandler);

		return http.build();
	}
}
