package org.prgrms.wumo.domain.party.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 목록 정보")
public record PartyGetAllResponse(

		@Schema(description = "모임 목록", requiredMode = Schema.RequiredMode.REQUIRED)
		List<PartyGetResponse> party,

		@Schema(description = "커서 식별자", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
		Long lastId

) {
}
