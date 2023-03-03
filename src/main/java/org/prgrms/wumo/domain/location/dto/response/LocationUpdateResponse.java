package org.prgrms.wumo.domain.location.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import org.prgrms.wumo.domain.location.model.Category;

@Schema(name = "후보지 수정 응답 정보")
public record LocationUpdateResponse(
		@Schema(description = "수정된 후보장소 식별자", example = "1")
		Long id,

		@Schema(description = "후보지 이미지", example = "DRINK")
		String name,

		@Schema(description = "후보지 검색용 주소", example = "경기도 고양시")
		String searchAddress,

		@Schema(description = "후보지 주소", example = "경기도 고양시 일산서구 ~")
		String address,

		@Schema(description = "후보지 위도", example = "12.345")
		Float latitude,

		@Schema(description = "후보지 경도", example = "67.890")
		Float longitude,

		@Schema(description = "후보지 이미지", example = "http://~.png")
		String image,

		@Schema(description = "후보지 설명", example = "바나나맛이 맛있다고함!")
		String description,

		@Schema(description = "후보지 방문 예정일 수정 내용", example = "2023-02-25T18:00:00")
		LocalDateTime visitDate,

		@Schema(description = "후보지 예상 지출 수정 내용", example = "70000")
		int expectedCost,

		@Schema(description = "후보지 카테고리", example = "DRINK")
		Category category
) {
}
