package org.prgrms.wumo.domain.party.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 생성 응답 정보")
public record PartyRegisterResponse(

		@Schema(description = "생성된 모임 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long id

) {
}
