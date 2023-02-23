package org.prgrms.wumo.domain.location.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "후보장소 등록 응답 정보")
public record LocationRegisterResponse(
		@Schema(description = "등록된 후보장소 식별자", example = "1", required = true)
		Long id
) {
}
