package org.prgrms.wumo.domain.party.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 구성원 정보")
public record PartyMemberGetResponse(

		@Schema(description = "사용자 식별자", example = "1", required = true)
		Long memberId,

		@Schema(description = "닉네임", example = "오예스", required = true)
		String nickname,

		@Schema(description = "역할", example = "총무", required = true)
		String role,

		@Schema(description = "프로필 이미지", example = "https://~.jpeg", required = true)
		String profileImage

) {
}
