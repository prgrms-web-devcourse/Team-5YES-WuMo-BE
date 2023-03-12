package org.prgrms.wumo.domain.route.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "루트 목록 조회 응답 시 각각의 루트 정보")
public record RouteGetAllResponse(
		@Schema(description = "루트 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		long routeId,

		@Schema(description = "루트의 좋아요 수", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
		long likeCount,

		@Schema(description = "로그인 중인 회원이 이 루트를 좋아요 눌렀는지 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
		boolean isLiking,

		@Schema(description = "루트의 장소 이름들", requiredMode = Schema.RequiredMode.REQUIRED)
		List<RouteLocationSimpleResponse> locations,

		@Schema(description = "루트 이름", example = "퇴사 기념 여행", requiredMode = Schema.RequiredMode.REQUIRED)
		String name,

		@Schema(description = "루트가 속한 모임 시작일", example = "2023-02-21T10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
		LocalDateTime startDate,

		@Schema(description = "루트가 속한 모임 마지막일", example = "2023-02-25T10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
		LocalDateTime endDate,

		@Schema(description = "루트의 썸네일 이미지", example = "https://~", requiredMode = Schema.RequiredMode.REQUIRED)
		String image,

		@Schema(description = "루트가 속한 모임 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long partyId
) {
}
