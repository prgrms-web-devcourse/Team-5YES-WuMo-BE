package org.prgrms.wumo.domain.comment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.prgrms.wumo.domain.comment.dto.request.PrivateRouteCommentGetAllRequest;
import org.prgrms.wumo.domain.comment.dto.request.PrivateRouteCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.request.PrivateRouteCommentUpdateRequest;
import org.prgrms.wumo.domain.comment.dto.request.PublicRouteCommentGetAllRequest;
import org.prgrms.wumo.domain.comment.dto.request.PublicRouteCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.request.PublicRouteCommentUpdateRequest;
import org.prgrms.wumo.domain.comment.dto.response.PrivateRouteCommentGetAllResponse;
import org.prgrms.wumo.domain.comment.dto.response.PrivateRouteCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.dto.response.PublicRouteCommentGetAllResponse;
import org.prgrms.wumo.domain.comment.dto.response.PublicRouteCommentRegisterResponse;
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

@RestController
@RequestMapping("/api/v1/route-comments")
@Tag(name = "루트 댓글 api")
public class RouteCommentController {

	@PostMapping("/private")
	@Operation(summary = "비공개 루트 댓글 생성")
	public ResponseEntity<PrivateRouteCommentRegisterResponse> registerPrivateRouteComment(
			@RequestBody
			@Valid
			PrivateRouteCommentRegisterRequest request
	) {
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}

	@PostMapping("/public")
	@Operation(summary = "공개 루트 댓글 생성")
	public ResponseEntity<PublicRouteCommentRegisterResponse> registerPublicRouteComment(
			@RequestBody
			@Valid
			PublicRouteCommentRegisterRequest request
	) {
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}

	@GetMapping("/private")
	@Operation(summary = "비공개 루트 댓글 전체 조회")
	public ResponseEntity<PrivateRouteCommentGetAllResponse> getAllPrivateRouteComment(
			@RequestBody
			@Valid
			PrivateRouteCommentGetAllRequest request
	) {
		return ResponseEntity.ok(null);
	}

	@GetMapping("/public")
	@Operation(summary = "공개 루트 댓글 전체 조회")
	public ResponseEntity<PublicRouteCommentGetAllResponse> getAllPublicRouteComment(
			@RequestBody
			@Valid
			PublicRouteCommentGetAllRequest request
	) {
		return ResponseEntity.ok(null);
	}

	@PatchMapping("/private")
	@Operation(summary = "비공개 루트 댓글 수정")
	public ResponseEntity<Void> updatePrivateRouteComment(@RequestBody @Valid PrivateRouteCommentUpdateRequest request) {
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/public")
	@Operation(summary = "공개 루트 댓글 수정")
	public ResponseEntity<Void> updatePublicRouteComment(@RequestBody @Valid PublicRouteCommentUpdateRequest request) {
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/private/{id}")
	@Operation(summary = "비공개 루트 댓글 삭제")
	public ResponseEntity<Void> deletePrivateRouteComment(
			@PathVariable("id") @Parameter(description = "삭제하고자 하는 비공개 루트 댓글") Long id
	) {
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/public/{id}")
	@Operation(summary = "공개 루트 댓글 삭제")
	public ResponseEntity<Void> deletePublicRouteComment(
			@PathVariable("id") @Parameter(description = "삭제하고자 하는 공개 루트 댓글") Long id
	) {
		return ResponseEntity.ok().build();
	}

}
