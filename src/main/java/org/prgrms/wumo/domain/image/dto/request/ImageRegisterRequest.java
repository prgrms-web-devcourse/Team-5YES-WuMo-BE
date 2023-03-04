package org.prgrms.wumo.domain.image.dto.request;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "이미지 등록 요청 정보")
public record ImageRegisterRequest(

	@NotNull(message = "등록할 이미지가 필요합니다.")
	@Schema(description = "이미지 파일", requiredMode = Schema.RequiredMode.REQUIRED)
	MultipartFile image

) {
}
