package org.prgrms.wumo.domain.party.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "초대코드 유효성 검증 응답 정보")
public record InvitationValidateResponse(

		@Schema(description = "모임 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long partyId

) {
}
