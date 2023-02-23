package org.prgrms.wumo.domain.image.api;

import javax.validation.Valid;

import org.prgrms.wumo.domain.image.dto.request.ImageDeleteRequest;
import org.prgrms.wumo.domain.image.dto.request.ImageRegisterRequest;
import org.prgrms.wumo.domain.image.dto.response.ImageRegisterResponse;
import org.prgrms.wumo.domain.image.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
@Tag(name = "이미지 API")
public class ImageController {

	private final ImageService imageService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "이미지 등록")
	public ResponseEntity<ImageRegisterResponse> registerImage(
			@Valid ImageRegisterRequest imageRegisterRequest
	) {
		return new ResponseEntity<>(imageService.registerImage(imageRegisterRequest), HttpStatus.CREATED);
	}

	@DeleteMapping
	@Operation(summary = "이미지 삭제")
	public ResponseEntity<Void> deleteImage(
			@RequestBody @Valid ImageDeleteRequest imageDeleteRequest
	) {
		return ResponseEntity.ok().build();
	}

}