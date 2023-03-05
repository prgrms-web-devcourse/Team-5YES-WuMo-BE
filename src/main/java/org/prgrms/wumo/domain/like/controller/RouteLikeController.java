package org.prgrms.wumo.domain.like.controller;

import org.prgrms.wumo.domain.like.service.RouteLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/routes")
@Tag(name = "루트 좋아요 API")
public class RouteLikeController {

	private final RouteLikeService routeLikeService;

	@PostMapping("/{routeId}/likes")
	@Operation(summary = "루트 좋아요 등록")
	public ResponseEntity<Void> registerRouteLike(
			@PathVariable @Parameter(description = "루트 식별자", required = true) Long routeId
	) {
		routeLikeService.registerRouteLike(routeId);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@DeleteMapping("/{routeId}/likes")
	@Operation(summary = "루트 좋아요 삭제")
	public ResponseEntity<Void> deleteRouteLike(
			@PathVariable @Parameter(description = "루트 식별자", required = true) Long routeId
	) {
		routeLikeService.deleteRouteLike(routeId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
