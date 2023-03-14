package org.prgrms.wumo.global.oauth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.prgrms.wumo.domain.member.service.MemberService;
import org.prgrms.wumo.global.oauth.dto.OauthLoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * OAuth 로그인 성공 시 호출되는 handler
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OauthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Value("${front.server}")
	private String TARGET_URL;

	private final MemberService memberService;

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {

		if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
			log.info("oAuth2User -> {}", oAuth2User);
			String email = (String)oAuth2User.getAttributes().get("email");
			OauthLoginResponse oauthLoginResponse = memberService.registerOrGet(email);
			String targetUrl = determineTargetUrl(oauthLoginResponse);

			getRedirectStrategy().sendRedirect(request, response, targetUrl);
		}
	}

	private String determineTargetUrl(OauthLoginResponse oauthLoginResponse) {
		return UriComponentsBuilder.fromOriginHeader(TARGET_URL)
				.queryParam("accessToken", oauthLoginResponse.accessToken())
				.queryParam("refreshToken", oauthLoginResponse.refreshToken())
				.build()
				.toUriString();
	}
}
