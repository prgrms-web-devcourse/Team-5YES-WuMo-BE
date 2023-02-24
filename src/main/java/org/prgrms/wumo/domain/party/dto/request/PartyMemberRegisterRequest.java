package org.prgrms.wumo.domain.party.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 구성원 등록 요청 정보")
public record PartyMemberRegisterRequest(

		@NotNull
		@Schema(description = "참여 사용자 식별자", example = "1", required = true)
		Long memberId,

		@NotBlank
		@Schema(description = "역할", example = "총무", required = true)
		String role

) {
}
