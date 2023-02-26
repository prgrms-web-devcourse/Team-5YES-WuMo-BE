package org.prgrms.wumo.domain.party.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 상세 정보")
public record PartyGetDetailResponse(

		@Schema(description = "모임 식별자", example = "1", required = true)
		Long id,

		@Schema(description = "모임 이름", example = "오예스 워크샵", required = true)
		String name,

		@Schema(description = "시작일", example = "2023-02-21", required = true)
		LocalDateTime startDate,

		@Schema(description = "종료일", example = "2023-02-22", required = true)
		LocalDateTime endDate,

		@Schema(description = "종료일", example = "팀 설립 기념 워크샵", required = true)
		String description,

		@Schema(description = "이미지 경로", example = "https://~.jpeg", required = true)
		String coverImage

) {
}
