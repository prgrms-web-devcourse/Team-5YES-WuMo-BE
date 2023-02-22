package org.prgrms.wumo.domain.party.dto.request;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 참여 요청 정보")
public record PartyJoinRequest(

		@NotNull(message = "입장 비밀번호는 필수 입력사항입니다.")
		@Schema(description = "입장 비밀번호", example = "1234", required = true)
		String password
		
) {
}
