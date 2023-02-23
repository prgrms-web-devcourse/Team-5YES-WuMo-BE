package org.prgrms.wumo.domain.location.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(name = "후보장소 수정 요청 정보")
public record LocationUpdateRequest(
		@NotNull
		@Schema(description = "수정할 후보장소 식별자", example = "1", required = true)
		Long id,

		@NotBlank(message = "카테고리는 필수 입력사항입니다.")
		@Schema(description = "후보장소 카테고리 수정 내용", example = "DRINK", required = true)
		String category,

		@Schema(description = "후보장소 설명 수정 내용", example = "바나나맛이 맛있다고함!", required = false)
		String description,

		@NotNull(message = "방문 예정일은 필수 입력사항입니다.")
		@Schema(description = "후보장소 방문 예정일 수정 내용", example = "2023-02-25T18:00:00", required = true)
		LocalDateTime visitDate,

		@NotNull(message = "예상 지출은 필수 입력사항입니다.")
		@Schema(description = "후보장소 예상 지출 수정 내용", example = "70000", required = true)
		int expectedCost
) {
}
