package org.prgrms.wumo.domain.route.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "루트 상세 조회 응답 정보")
public record RouteGetResponse(
	@Schema(description = "루트 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	Long id,

	@Schema(description = "루트 이름", example = "퇴사 기념 여행", requiredMode = Schema.RequiredMode.REQUIRED)
	String name,

	@Schema(description = "루트의 현재 공개 여부", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
	boolean isPublic,

	@Schema(description = "루트의 장소들", requiredMode = Schema.RequiredMode.REQUIRED)
	List<RouteLocationResponse> locations,

	@Schema(description = "루트가 속한 모임 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	Long partyId
) {
}
