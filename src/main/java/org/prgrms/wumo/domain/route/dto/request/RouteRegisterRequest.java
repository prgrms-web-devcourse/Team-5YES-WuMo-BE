package org.prgrms.wumo.domain.route.dto.request;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "루트에 후보지 등록 요청 정보")
public record RouteRegisterRequest(
	@NotNull(message = "등록할 후보지를 선택해주세요.")
	@Schema(description = "루트에 등록할 후보지 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	Long locationId,

	@NotNull(message = "루트를 등록할 모임을 선택해주세요.")
	@Schema(description = "루트를 등록할 모임 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	Long partyId
) {
}
