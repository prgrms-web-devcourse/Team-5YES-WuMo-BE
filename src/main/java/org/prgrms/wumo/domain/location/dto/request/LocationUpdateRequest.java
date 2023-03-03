package org.prgrms.wumo.domain.location.dto.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.prgrms.wumo.domain.location.model.Category;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "후보지 수정 요청 정보")
public record LocationUpdateRequest(
		@NotNull
		@Schema(description = "수정할 후보지 식별자", example = "1", required = true)
		Long id,

		@NotNull(message = "사진은 필수 입력사항입니다.")
		@Schema(description = "후보지 사진", example = "http://~.png", required = true)
		String image,

		@NotNull(message = "카테고리는 필수 입력사항입니다.")
		@Schema(description = "후보지 카테고리 수정 내용", example = "DRINK", required = true)
		Category category,

		@Schema(description = "후보장소 설명 수정 내용", example = "바나나맛이 맛있다고함!", required = false)
		String description,

		@NotNull(message = "방문 예정일은 필수 입력사항입니다.")
		@Schema(description = "후보지 방문 예정일 수정 내용", example = "2023-02-25T18:00:00", required = true)
		LocalDateTime visitDate,

		@NotNull(message = "예상 지출은 필수 입력사항입니다.")
		@Schema(description = "후보지 예상 지출 수정 내용", example = "70000", required = true)
		int expectedCost,

		@NotNull(message = "수정된 후보지가 포함된 모임 식별자는 필수 입력사항입니다.")
		@Schema(description = "수정한 후보지가 속한 모임 식별자", example = "1", required = true)
		Long partyId
) {
}
