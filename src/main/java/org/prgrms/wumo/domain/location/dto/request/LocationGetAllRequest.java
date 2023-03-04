package org.prgrms.wumo.domain.location.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "후보지 목록 조회 요청 정보")
public record LocationGetAllRequest(
		@Schema(description = "커서 식별자", example = "5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
		Long cursorId,

		@NotNull
		@Positive(message = "page size는 0 또는 음수일 수 없습니다.")
		@Schema(description = "페이지 사이즈", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
		int pageSize,

		@NotNull
		@Schema(description = "후보지들이 속한 모임 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long partyId
) {
}
