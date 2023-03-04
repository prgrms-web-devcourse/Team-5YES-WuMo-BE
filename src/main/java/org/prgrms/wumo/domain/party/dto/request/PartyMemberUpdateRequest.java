package org.prgrms.wumo.domain.party.dto.request;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 구성원 수정 요청 정보")
public record PartyMemberUpdateRequest(

		@NotBlank
		@Schema(description = "역할", example = "총무", requiredMode = Schema.RequiredMode.REQUIRED)
		String role

) {
}
