package org.prgrms.wumo.domain.image.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "이미지 등록 응답 정보")
public record ImageRegisterResponse(

	@Schema(description = "이미지 경로", example = "https://~.jpeg", requiredMode = Schema.RequiredMode.REQUIRED)
	String imageUrl

) {
}
