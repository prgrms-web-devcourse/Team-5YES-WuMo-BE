package org.prgrms.wumo.domain.route.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "루트의 후보지 응답 정보")
public record RouteLocationResponse(
	@Schema(description = "후보지 식별자", example = "1", required = true)
	Long id,

	@Schema(description = "후보지 상호명", example = "오예스 식당", required = true)
	String name,

	@Schema(description = "후보지 주소", example = "부산광역시 수영구 ~~~ ", required = true)
	String address,

	@Schema(description = "후보지 위도", example = "34.567890", required = true)
	Float latitude,

	@Schema(description = "후보지 경도", example = "123.567890", required = true)
	Float longitude,

	@Schema(description = "후보지 이미지", example = "https://~", required = true)
	String image,

	@Schema(description = "후보지 설명", example = "딸기맛이 맛있다고함!", required = false)
	String description,

	@Schema(description = "후보지 방문예정일", example = "2023-02-21T10:00:00", required = true)
	LocalDateTime visitDate,

	@Schema(description = "후보지 예상 지출", example = "55000", required = true)
	int expectedCost,

	@Schema(description = "후보지 실제 지출", example = "55000", required = false)
	int spending,

	@Schema(description = "후보지 카테고리", example = "MEAL", required = true)
	String category
) {
}
