package org.prgrms.wumo.domain.location.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import org.prgrms.wumo.domain.location.model.Category;

@Schema(name = "후보지 수정 응답 정보")
public record LocationUpdateResponse(
		@Schema(description = "수정된 후보지 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long id,

		@Schema(description = "후보지 이미지", example = "http://~.png", requiredMode = Schema.RequiredMode.REQUIRED)
		String image,

		@Schema(description = "후보지 설명", example = "바나나맛이 맛있다고함!", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
		String description,

		@Schema(description = "후보지 방문 예정일 수정 내용", example = "2023-02-25T18:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
		LocalDateTime visitDate,

		@Schema(description = "후보지 예상 지출 수정 내용", example = "70000", requiredMode = Schema.RequiredMode.REQUIRED)
		int expectedCost,

		@Schema(description = "후보지 카테고리", example = "DRINK", requiredMode = Schema.RequiredMode.REQUIRED)
		Category category
) {
}
