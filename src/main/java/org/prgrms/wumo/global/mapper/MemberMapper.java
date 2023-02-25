package org.prgrms.wumo.global.mapper;

import org.prgrms.wumo.domain.member.dto.request.MemberRegisterRequest;
import org.prgrms.wumo.domain.member.dto.response.MemberRegisterResponse;
import org.prgrms.wumo.domain.member.model.Member;

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
}
