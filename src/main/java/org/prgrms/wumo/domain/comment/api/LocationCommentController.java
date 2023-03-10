package org.prgrms.wumo.domain.comment.api;

import javax.validation.Valid;

import org.prgrms.wumo.domain.comment.dto.request.LocationCommentGetAllRequest;
import org.prgrms.wumo.domain.comment.dto.request.LocationCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.request.LocationCommentUpdateRequest;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentGetAllResponse;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentUpdateResponse;
import org.prgrms.wumo.domain.comment.service.LocationCommentService;
import org.prgrms.wumo.domain.comment.service.ReplyCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/location-comments")
@Tag(name = "후보지 댓글 api")
public class LocationCommentController {
	private final LocationCommentService locationCommentService;
	private final ReplyCommentService replyCommentService;

	@PostMapping
	@Operation(summary = "후보지 댓글 등록")
	public ResponseEntity<LocationCommentRegisterResponse> registerLocationComment(
			@RequestBody @Valid LocationCommentRegisterRequest locationCommentRegisterRequest
	) {
		return new ResponseEntity<>(locationCommentService.registerLocationComment(locationCommentRegisterRequest),
				HttpStatus.CREATED);
	}

	@GetMapping
	@Operation(summary = "후보지 댓글 조회")
	public ResponseEntity<LocationCommentGetAllResponse> getAllLocationComment(
			@Valid LocationCommentGetAllRequest locationCommentGetAllRequest
	) {
		return ResponseEntity.ok(locationCommentService.getAllLocationComments(locationCommentGetAllRequest));
	}

	@PatchMapping
	@Operation(summary = "후보지 댓글 수정")
	public ResponseEntity<LocationCommentUpdateResponse> updateLocationComment(
			@RequestBody @Valid LocationCommentUpdateRequest locationCommentUpdateRequest
	) {
		return ResponseEntity.ok(locationCommentService.updateLocationComment(locationCommentUpdateRequest));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "후보지 댓글 삭제")
	public ResponseEntity<Void> deleteLocationComment(
			@PathVariable("id") @Parameter(description = "삭제하고자 하는 후보지 댓글 식별자") Long locationCommentId
	) {

		locationCommentService.deleteLocationComment(locationCommentId);
		return ResponseEntity.ok().build();
	}
}
