package org.prgrms.wumo.domain.party.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 목록 조회 요청 정보")
public record PartyGetRequest(

		@Schema(description = "커서 식별자", required = false, example = "5")
		Long cursorId,

		@NotNull
		@Positive(message = "page size는 0 또는 음수일 수 없습니다.")
		@Schema(description = "페이지 사이즈", required = true, example = "5")
		int pageSize

) {
}
