package org.prgrms.wumo.domain.comment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentGetAllRequest;
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentUpdateRequest;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentGetAllResponse;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentUpdateResponse;
import org.prgrms.wumo.domain.comment.service.PartyRouteCommentService;
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
@RequestMapping("/api/v1/party-route-comments")
@RequiredArgsConstructor
@Tag(name = "루트 댓글 api")
public class RouteCommentController {

	private final PartyRouteCommentService partyRouteCommentService;

	@PostMapping
	@Operation(summary = "모임 내 루트 댓글 생성")
	public ResponseEntity<PartyRouteCommentRegisterResponse> registerPartyRouteComment(
			@RequestBody @Valid PartyRouteCommentRegisterRequest request
	) {
		return new ResponseEntity<>(partyRouteCommentService.registerPartyRouteComment(request), HttpStatus.CREATED);
	}

	@GetMapping
	@Operation(summary = "모임 내 루트 댓글 목록 조회")
	public ResponseEntity<PartyRouteCommentGetAllResponse> getAllPartyRouteComment(
			@Valid PartyRouteCommentGetAllRequest request
	) {
		return ResponseEntity.ok(partyRouteCommentService.getAllPartyRouteComment(request));
	}

	@PatchMapping
	@Operation(summary = "모임 내 루트 댓글 수정")
	public ResponseEntity<PartyRouteCommentUpdateResponse> updatePrivateRouteComment(
			@RequestBody @Valid PartyRouteCommentUpdateRequest request
	) {
		return ResponseEntity.ok(partyRouteCommentService.updatePartyRouteComment(request));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "모임 내 루트 댓글 삭제")
	public ResponseEntity<Void> deletePrivateRouteComment(
			@PathVariable("id") @Parameter(description = "삭제하고자 하는 비공개 루트 댓글") Long id
	) {
		return ResponseEntity.ok().build();
	}

}
