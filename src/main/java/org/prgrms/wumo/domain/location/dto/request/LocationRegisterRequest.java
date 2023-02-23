package org.prgrms.wumo.domain.location.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(name = "후보장소 등록 요청 정보")
public record LocationRegisterRequest(
		@NotBlank(message = "상호명은 필수 입력사항입니다.")
		@Size(max = 50, message = "상호명은 {max}글자 이하로 입력할 수 있습니다.")
		@Schema(description = "후보장소 상호명", example = "오예스 식당", required = true)
		String name,

		@NotBlank(message = "주소 필수 입력사항입니다.")
		@Schema(description = "후보장소 주소", example = "부산광역시 수영구 ~~~ ", required = true)
		String address,

		@NotBlank(message = "위도는 필수 입력사항입니다.")
		@Schema(description = "후보장소 위도", example = "34.567890", required = true)
		String latitude,

		@NotBlank(message = "경도는 필수 입력사항입니다.")
		@Schema(description = "후보장소 경도", example = "123.567890", required = true)
		String longitude,

		@NotBlank(message = "이미지는 필수 입력사항입니다.")
		@Schema(description = "후보장소 이미지", example = "https://~", required = true)
		String image,

		@NotBlank(message = "카테고리는 필수 입력사항입니다.")
		@Schema(description = "후보장소 카테고리", example = "MEAL", required = true)
		String category,

		@Schema(description = "후보장소 설명", example = "딸기맛이 맛있다고함!", required = false)
		String description,

		@NotNull(message = "방문 예정일은 필수 입력사항입니다.")
		@Schema(description = "후보장소 방문 예정일", example = "2023-02-21T10:00:00", required = true)
		LocalDateTime visitDate,

		@NotNull(message = "예상 지출은 필수 입력사항입니다.")
		@Schema(description = "후보장소 예상 지출", example = "50000", required = true)
		int expectedCost
) {
}
