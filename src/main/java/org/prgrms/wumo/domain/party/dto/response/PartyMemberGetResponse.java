package org.prgrms.wumo.domain.party.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 구성원 정보")
public record PartyMemberGetResponse(

		@Schema(description = "사용자 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long memberId,

		@Schema(description = "닉네임", example = "오예스", requiredMode = Schema.RequiredMode.REQUIRED)
		String nickname,

		@Schema(description = "역할", example = "총무", requiredMode = Schema.RequiredMode.REQUIRED)
		String role,

		@Schema(description = "프로필 이미지", example = "https://~.jpeg", requiredMode = Schema.RequiredMode.REQUIRED)
		String profileImage,

		@Schema(description = "모임장 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
		boolean isLeader

) {
}
