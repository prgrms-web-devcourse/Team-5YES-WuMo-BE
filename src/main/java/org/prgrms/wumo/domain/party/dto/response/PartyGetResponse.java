package org.prgrms.wumo.domain.party.dto.response;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 상세 정보")
public record PartyGetResponse(

		@Schema(description = "모임 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long id,

		@Schema(description = "모임 이름", example = "오예스 워크샵", requiredMode = Schema.RequiredMode.REQUIRED)
		String name,

		@Schema(description = "시작일", example = "2023-02-21", requiredMode = Schema.RequiredMode.REQUIRED)
		LocalDate startDate,

		@Schema(description = "종료일", example = "2023-02-22", requiredMode = Schema.RequiredMode.REQUIRED)
		LocalDate endDate,

		@Schema(description = "종료일", example = "팀 설립 기념 워크샵", requiredMode = Schema.RequiredMode.REQUIRED)
		String description,

		@Schema(description = "이미지 경로", example = "https://~.jpeg", requiredMode = Schema.RequiredMode.REQUIRED)
		String coverImage

) {
}
