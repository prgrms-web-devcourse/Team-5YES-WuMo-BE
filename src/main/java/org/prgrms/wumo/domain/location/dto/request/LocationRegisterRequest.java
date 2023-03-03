package org.prgrms.wumo.domain.location.dto.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.prgrms.wumo.domain.location.model.Category;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "후보지 등록 요청 정보")
public record LocationRegisterRequest(
		@NotBlank(message = "상호명은 필수 입력사항입니다.")
		@Size(max = 50, message = "상호명은 {max}글자 이하로 입력할 수 있습니다.")
		@Schema(description = "후보지 상호명", example = "오예스 식당", required = true)
		String name,

		@NotNull(message = "주소 필수 입력사항입니다.")
		@Schema(description = "후보지 주소", example = "부산광역시", required = true)
		String searchAddress,

		@NotNull(message = "주소 필수 입력사항입니다.")
		@Schema(description = "후보지 주소", example = "부산광역시 수영구 ~~~ ", required = true)
		String address,

		@NotNull(message = "위도는 필수 입력사항입니다.")
		@Schema(description = "후보지 위도", example = "34.566", required = true)
		Float latitude,

		@NotNull(message = "경도는 필수 입력사항입니다.")
		@Schema(description = "후보지 경도", example = "123.56", required = true)
		Float longitude,

		@NotBlank(message = "이미지는 필수 입력사항입니다.")
		@Schema(description = "후보지 이미지", example = "https://~", required = true)
		String image,

		@NotNull(message = "카테고리는 필수 입력사항입니다.")
		@Schema(description = "후보지 카테고리", example = "MEAL", required = true)
		Category category,

		@Schema(description = "후보지 설명", example = "딸기맛이 맛있다고함!", required = false)
		String description,

		@NotNull(message = "방문 예정일은 필수 입력사항입니다.")
		@Schema(description = "후보지 방문 예정일", example = "2023-02-21T10:00:00", required = true)
		LocalDateTime visitDate,

		@NotNull(message = "예상 지출은 필수 입력사항입니다.")
		@Schema(description = "후보지 예상 지출", example = "50000", required = true)
		int expectedCost,

		@NotNull(message = "후보지가 등록된 모임의 식별자는 필수 입력사항입니다")
		@Schema(description = "후보지가 등록된 모임 식별자", example = "1", required = true)
		Long partyId
) {
}
