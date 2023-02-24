package org.prgrms.wumo.domain.party.dto.request;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 구성원 수정 요청 정보")
public record PartyMemberUpdateRequest(

		@NotBlank
		@Schema(description = "역할", required = true, example = "총무")
		String role

) {
}
