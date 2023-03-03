package org.prgrms.wumo.domain.route.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "루트 목록 조회 응답 정보")
public record RouteGetAllResponses(
	@Schema(description = "루트 목록", requiredMode = Schema.RequiredMode.REQUIRED)
	List<RouteGetAllResponse> routes,

	@Schema(description = "다음 커서 식별자", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
	Long lastId
) {
}
