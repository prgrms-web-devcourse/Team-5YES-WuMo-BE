package org.prgrms.wumo.domain.party.dto.request;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 구성원 등록 요청 정보")
public record PartyMemberRegisterRequest(

		@Length(min = 0, max = 10, message = "역할은 {min}자 이상 {max}자 이하만 가능합니다.")
		@Schema(description = "역할", example = "총무", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
		String role

) {
}
