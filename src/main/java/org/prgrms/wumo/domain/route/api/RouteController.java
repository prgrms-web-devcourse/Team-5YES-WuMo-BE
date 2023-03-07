package org.prgrms.wumo.domain.route.api;

import javax.validation.Valid;

import org.prgrms.wumo.domain.route.dto.request.RouteGetAllRequest;
import org.prgrms.wumo.domain.route.dto.request.RouteRegisterRequest;
import org.prgrms.wumo.domain.route.dto.request.RouteStatusUpdateRequest;
import org.prgrms.wumo.domain.route.dto.response.RouteGetAllResponses;
import org.prgrms.wumo.domain.route.dto.response.RouteGetResponse;
import org.prgrms.wumo.domain.route.dto.response.RouteRegisterResponse;
import org.prgrms.wumo.domain.route.service.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
@Tag(name = "루트 API")
public class RouteController {

	private final RouteService routeService;

	@PostMapping
	@Operation(summary = "루트에 후보지 등록")
	public ResponseEntity<RouteRegisterResponse> registerRoute(
		@RequestBody @Valid RouteRegisterRequest routeRegisterRequest) {

		return new ResponseEntity<>(routeService.registerRoute(routeRegisterRequest), HttpStatus.CREATED);
	}

	@GetMapping("/{partyId}")
	@Operation(summary = "루트 상세 조회")
	public ResponseEntity<RouteGetResponse> getRoute(
		@PathVariable @Parameter(description = "조회할 루트가 포함된 모임 아이디") long partyId,
		@RequestParam("path") @Parameter(description = "접근 경로(모임에서이면 0, 공개 목록에서이면 1)") int fromPublic) {

		return ResponseEntity.ok(routeService.getRoute(partyId, fromPublic));
	}

	@GetMapping
	@Operation(summary = "공개된 루트 목록 조회")
	public ResponseEntity<RouteGetAllResponses> getAllRoute(
		@Valid RouteGetAllRequest routeGetAllRequest) {

		return ResponseEntity.ok(routeService.getAllRoute(routeGetAllRequest));
	}

	@PatchMapping
	@Operation(summary = "루트 공개여부 변경")
	public ResponseEntity<Void> updateRoutePublicStatus(
		@RequestBody @Valid RouteStatusUpdateRequest routeStatusUpdateRequest) {

		routeService.updateRoutePublicStatus(routeStatusUpdateRequest);
		return ResponseEntity.ok().build();
	}

}
