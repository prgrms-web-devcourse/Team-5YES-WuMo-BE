package org.prgrms.wumo.domain.image.dto.request;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "이미지 삭제 요청 정보")
public record ImageDeleteRequest(

		@NotBlank(message = "삭제할 이미지 경로가 필요합니다.")
		@Schema(description = "이미지 경로", required = true)
		String imageUrl

) {
}
