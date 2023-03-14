package org.prgrms.wumo.global.oauth.service;

import java.util.Collections;

import org.prgrms.wumo.global.oauth.model.Oauth2Attributes;
import org.prgrms.wumo.global.oauth.model.SocialOauthType;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * provider로부터 받은 회원 정보를 가공
 */
@Service
@RequiredArgsConstructor
public class CustomOauthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		SocialOauthType socialOauthType = SocialOauthType.valueOf(registrationId.toUpperCase());

		String userNameAttributeName = userRequest
				.getClientRegistration()
				.getProviderDetails()
				.getUserInfoEndpoint()
				.getUserNameAttributeName();

		Oauth2Attributes oauth2Attributes = Oauth2Attributes.of(socialOauthType, oAuth2User.getAttributes());

		return new DefaultOAuth2User(
				Collections.EMPTY_LIST,
				oauth2Attributes.convertToMap(),
				userNameAttributeName
		);
	}
}
