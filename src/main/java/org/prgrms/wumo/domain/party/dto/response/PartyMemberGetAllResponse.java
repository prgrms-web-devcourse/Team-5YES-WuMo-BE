package org.prgrms.wumo.domain.party.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 구성원 정보 목록")
public record PartyMemberGetAllResponse(
		@Schema(description = "모임 구성원 목록")
		List<PartyMemberGetResponse> members,

		@Schema(description = "커서 아이디", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
		Long lastId
) {
}
