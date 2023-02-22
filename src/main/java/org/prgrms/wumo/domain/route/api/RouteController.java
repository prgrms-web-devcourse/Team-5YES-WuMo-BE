package org.prgrms.wumo.domain.route.api;

import javax.validation.Valid;

import org.prgrms.wumo.domain.route.dto.request.RouteGetAllRequest;
import org.prgrms.wumo.domain.route.dto.request.RouteRegisterRequest;
import org.prgrms.wumo.domain.route.dto.response.RouteGetAllResponse;
import org.prgrms.wumo.domain.route.dto.response.RouteGetResponse;
import org.prgrms.wumo.domain.route.dto.response.RouteRegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/routes")
@Tag(name = "루트 API")
public class RouteController {

	@PostMapping
	@Operation(summary = "루트에 후보지 등록")
	public ResponseEntity<RouteRegisterResponse> registerRoute(
		@RequestBody @Valid RouteRegisterRequest routeRegisterRequest) {

		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}

	@GetMapping("/{routeId}")
	@Operation(summary = "루트 상세 조회")
	public ResponseEntity<RouteGetResponse> getRoute(
		@PathVariable @Parameter(description = "조회할 루트 아이디") Long routeId) {

		return ResponseEntity.ok(null);
	}

	@GetMapping
	@Operation(summary = "루트 목록 조회")
	public ResponseEntity<RouteGetAllResponse> getAllRoute(
		@Valid RouteGetAllRequest routeGetAllRequest) {

		return ResponseEntity.ok(null);
	}

	@DeleteMapping
	@Operation(summary = "루트에서 후보지 삭제")
	public ResponseEntity<Void> deleteLocation(
		@RequestParam @Parameter(description = "루트에서 삭제할 후보장소 아이디") Long locationId) {

		return ResponseEntity.ok().build();
	}
}