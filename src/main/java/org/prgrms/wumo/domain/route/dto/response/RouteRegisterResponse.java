package org.prgrms.wumo.domain.route.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "루트에 후보지 등록 응답 정보")
public record RouteRegisterResponse(
	@Schema(description = "등록된 루트 식별자", example = "1", required = true)
	Long id
) {
}
