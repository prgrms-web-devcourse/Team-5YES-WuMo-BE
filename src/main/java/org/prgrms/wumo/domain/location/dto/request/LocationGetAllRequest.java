package org.prgrms.wumo.domain.location.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Schema(name = "후보장소 목록 조회 요청 정보")
public record LocationGetAllRequest(
		@Schema(description = "커서 식별자", required = false, example = "5")
		Long cursorId,

		@NotNull
		@Positive(message = "page size는 0 또는 음수일 수 없습니다.")
		@Schema(description = "페이지 사이즈", required = true, example = "5")
		int pageSize,

		@NotNull
		@Schema(description = "후보 장소들이 속한 Party 식별자", required = true, example = "1")
		Long partyId
) {
}
