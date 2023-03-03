package org.prgrms.wumo.domain.location.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.prgrms.wumo.domain.location.model.Category;

@Schema(name = "후보지 수정 요청 정보")
public record LocationUpdateRequest(
		@NotNull
		@Schema(description = "수정할 후보장소 식별자", example = "1", required = true)
		Long id,

		@NotBlank(message = "이름은 필수사항입니다")
		@Schema(description = "후보지 이름", example = "빽다방", required = true)
		String name,

		@NotNull(message = "검색용 주소는 필수 입력사항입니다.")
		@Schema(description = "후보지 주소", example = "경기도 고양시", required = true)
		String searchAddress,

		@NotNull(message = "주소는 필수사항입니다")
		@Schema(description = "후보지 주소", example = "경기도 고양시 일산서구 ~", required = true)
		String address,

		@NotNull(message = "위도는 필수사항입니다")
		@Schema(description = "후보지 위도", example = "12.345", required = true)
		Float latitude,

		@NotNull(message = "경도는 필수사항입니다")
		@Schema(description = "후보지 경도", example = "67.890", required = true)
		Float longitude,

		@NotNull(message = "이미지는 필수 입력사항입니다")
		@Schema(description = "후보지 이미지", example = "http://~.png", required = true)
		String image,

		@Schema(description = "후보지 설명", example = "바나나맛이 맛있다고함!", required = false)
		String description,

		@NotNull(message = "방문 예정일은 필수 입력사항입니다.")
		@Schema(description = "후보지 방문 예정일 수정 내용", example = "2023-02-25T18:00:00", required = true)
		LocalDateTime visitDate,

		@NotNull(message = "예상 지출은 필수 입력사항입니다.")
		@Schema(description = "후보지 예상 지출 수정 내용", example = "70000", required = true)
		int expectedCost,

		@NotNull(message = "카테고리는 필수 입력사항입니다.")
		@Schema(description = "후보지 카테고리", example = "DRINK", required = true)
		Category category,

		@NotNull(message = "수정하는 모임의 식별자는 필수 입력사항입니다.")
		@Schema(description = "후보지를 수정하는 모임 식별자", example = "1", required = true)
		Long partyId
) {
}
