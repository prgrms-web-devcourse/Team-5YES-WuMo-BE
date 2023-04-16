package org.prgrms.wumo.domain.member.mapper;

import org.prgrms.wumo.domain.member.dto.request.MemberRegisterRequest;
import org.prgrms.wumo.domain.member.dto.response.MemberGetResponse;
import org.prgrms.wumo.domain.member.dto.response.MemberLoginResponse;
import org.prgrms.wumo.domain.member.dto.response.MemberRegisterResponse;
import org.prgrms.wumo.domain.member.dto.response.MemberTokenResponse;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.global.jwt.WumoJwt;
import org.prgrms.wumo.global.oauth.dto.OauthLoginResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberMapper {

	public static Member toMember(MemberRegisterRequest memberRegisterRequest) {
		return Member.builder()
				.email(memberRegisterRequest.email())
				.nickname(memberRegisterRequest.nickname())
				.password(memberRegisterRequest.password())
				.build();
	}

	public static MemberRegisterResponse toMemberRegisterResponse(Long memberId) {
		return new MemberRegisterResponse(memberId);
	}

	public static MemberLoginResponse toMemberLoginResponse(MemberTokenResponse memberTokenResponse) {
		return new MemberLoginResponse(
				memberTokenResponse.grantType(),
				memberTokenResponse.accessToken());
	}

	public static MemberTokenResponse toMemberTokenResponse(WumoJwt wumoJwt) {
		return new MemberTokenResponse(
				wumoJwt.getGrantType(),
				wumoJwt.getAccessToken(),
				wumoJwt.getRefreshToken());
	}

	public static MemberGetResponse toMemberGetResponse(Member member) {
		return new MemberGetResponse(
				member.getId(),
				member.getEmail().getEmail(),
				member.getNickname(),
				member.getProfileImage());
	}

	public static Member toMember(String email) {
		return new Member(email, email);
	}

	public static OauthLoginResponse toOauthLoginResponse(WumoJwt wumoJwt) {
		return new OauthLoginResponse(
				wumoJwt.getGrantType(),
				wumoJwt.getAccessToken(),
				wumoJwt.getRefreshToken());
	}
}
