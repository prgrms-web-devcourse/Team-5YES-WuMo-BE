package org.prgrms.wumo.domain.party.dto.request;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 구성원 등록 요청 정보")
public record PartyMemberRegisterRequest(

		@NotBlank
		@Schema(description = "역할", example = "총무", required = true)
		String role

) {
}
