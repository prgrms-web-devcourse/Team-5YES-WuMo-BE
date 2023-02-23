package org.prgrms.wumo.domain.location.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(name = "후보장소 상세 조회 응답 정보")
public record LocationGetResponse(
		@Schema(description = "후보장소 식별자", example = "1", required = true)
		Long id,

		@Schema(description = "후보장소 상호명", example = "오예스 식당", required = true)
		String name,

		@Schema(description = "후보장소 주소", example = "부산광역시 수영구 ~~~ ", required = true)
		String address,

		@Schema(description = "후보장소 위도", example = "34.567890", required = true)
		String latitude,

		@Schema(description = "후보장소 경도", example = "123.567890", required = true)
		String longitude,

		@Schema(description = "후보장소 이미지", example = "https://~", required = true)
		String image,

		@Schema(description = "후보장소 설명", example = "딸기맛이 맛있다고함!", required = false)
		String description,

		@Schema(description = "후보장소 방문예정일", example = "2023-02-21T10:00:00", required = true)
		LocalDateTime visitDate,

		@Schema(description = "후보장소 예상 지출", example = "50000", required = true)
		int expectedCost,

		@Schema(description = "후보장소 실제 지출", example = "55000", required = false)
		int spending,

		@Schema(description = "후보장소 카테고리", example = "MEAL", required = true)
		String category,

		@Schema(description = "(후보장소가 루트라면) 루트번호", example = "1", required = false)
		Long routeId
) {
}