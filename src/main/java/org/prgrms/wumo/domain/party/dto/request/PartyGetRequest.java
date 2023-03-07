package org.prgrms.wumo.domain.party.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 목록 조회 요청 정보")
public record PartyGetRequest(

		@NotNull(message = "조회 옵션은 필수 입력사항입니다.")
		@Schema(description = "조회 옵션(ONGOING, COMPLETED, ALL)", example = "ONGOING", requiredMode = Schema.RequiredMode.REQUIRED)
		PartyType partyType,

		@Schema(description = "커서 식별자", example = "5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
		Long cursorId,

		@NotNull(message = "Page Size는 필수 입력사항입니다.")
		@Positive(message = "Page Size는 0 또는 음수일 수 없습니다.")
		@Schema(description = "페이지 사이즈", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
		int pageSize

) {
}
