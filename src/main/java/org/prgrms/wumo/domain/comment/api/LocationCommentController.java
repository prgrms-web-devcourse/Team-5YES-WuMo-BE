package org.prgrms.wumo.domain.comment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.prgrms.wumo.domain.comment.dto.request.LocationCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.request.LocationCommentUpdateRequest;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentRegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/location-comments")
@Tag(name = "후보지 댓글 api")
public class LocationCommentController {

	@PostMapping
	@Operation(summary = "후보지 댓글 등록")
	public ResponseEntity<LocationCommentRegisterResponse> registerLocationComment(
			@RequestBody
			@Valid
			LocationCommentRegisterRequest request
	) {
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}

	@PatchMapping
	@Operation(summary = "후보지 댓글 수정")
	public ResponseEntity<Void> updateLocationComment(@RequestBody @Valid LocationCommentUpdateRequest request){
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "후보지 댓글 삭제")
	public ResponseEntity<Void> deleteLocationComment(
			@PathVariable("id") @Parameter(description = "삭제하고자 하는 후보지 댓글") Long id
	) {
		return ResponseEntity.ok().build();
	}
}
