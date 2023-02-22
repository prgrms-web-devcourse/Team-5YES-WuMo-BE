package org.prgrms.wumo.domain.route.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "루트 목록 조회 응답 정보")
public record RouteGetAllResponse(
	@Schema(description = "루트의 좋아요 수", example = "10", required = true)
	long likeCount,

	@Schema(description = "루트의 장소 이름들", example = "[{갈치 구이집}, {바다 카페}]", required = true)
	List<String> locations,

	@Schema(description = "루트 썸네일", example = "https://~", required = true)
	String image,

	@Schema(description = "루트 이름", example = "퇴사 기념 여행", required = true)
	String name
) {
}
