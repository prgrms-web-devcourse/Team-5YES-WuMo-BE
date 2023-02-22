package org.prgrms.wumo.domain.party.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 목록 정보")
public record PartyGetResponse(

		@Schema(description = "모임 식별자", example = "1", required = true)
		Long id,

		@Schema(description = "모임 이름", example = "오예스 워크샵", required = true)
		String name,

		@Schema(description = "이미지 경로", example = "https://~.jpeg", required = true)
		String coverImage

) {
}
