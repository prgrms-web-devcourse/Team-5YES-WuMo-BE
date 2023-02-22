package org.prgrms.wumo.domain.party.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "생성된 초대 코드 정보")
public record InvitationRegisterResponse(

		@Schema(description = "초대 코드", example = "aBcDeFgH", required = true)
		String code

) {
}
