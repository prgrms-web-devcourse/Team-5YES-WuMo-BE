package org.prgrms.wumo.domain.party.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 구성원 정보 목록")
public record PartyMemberGetAllResponse(

		@Schema(description = "전체 모임 구성원 수", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
		Long totalMembers,

		@Schema(description = "모임 구성원 목록", requiredMode = Schema.RequiredMode.REQUIRED)
		List<PartyMemberGetResponse> members,

		@Schema(description = "커서 식별자", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
		Long lastId
) {
}
