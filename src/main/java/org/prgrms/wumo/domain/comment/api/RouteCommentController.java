package org.prgrms.wumo.domain.comment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentGetAllRequest;
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentUpdateRequest;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentGetAllResponse;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentRegisterResponse;
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

	@PostMapping("/party")
	@Operation(summary = "모임 내 루트 댓글 생성")
	public ResponseEntity<PartyRouteCommentRegisterResponse> registerPrivateRouteComment(
			@RequestBody @Valid PartyRouteCommentRegisterRequest request
	) {
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}

	@GetMapping("/party")
	@Operation(summary = "모임 내 루트 댓글 전체 조회")
	public ResponseEntity<PartyRouteCommentGetAllResponse> getAllPrivateRouteComment(
			@RequestBody @Valid PartyRouteCommentGetAllRequest request
	) {
		return ResponseEntity.ok(null);
	}

	@PatchMapping("/party")
	@Operation(summary = "모임 내 루트 댓글 수정")
	public ResponseEntity<Void> updatePrivateRouteComment(
			@RequestBody @Valid PartyRouteCommentUpdateRequest request
	) {
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/party/{id}")
	@Operation(summary = "모임 내 루트 댓글 삭제")
	public ResponseEntity<Void> deletePrivateRouteComment(
			@PathVariable("id") @Parameter(description = "삭제하고자 하는 비공개 루트 댓글") Long id
	) {
		return ResponseEntity.ok().build();
	}

}
