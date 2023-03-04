package org.prgrms.wumo.domain.location.api;

import javax.validation.Valid;

import org.prgrms.wumo.domain.location.dto.request.LocationGetAllRequest;
import org.prgrms.wumo.domain.location.dto.request.LocationRegisterRequest;
import org.prgrms.wumo.domain.location.dto.request.LocationSpendingUpdateRequest;
import org.prgrms.wumo.domain.location.dto.request.LocationUpdateRequest;
import org.prgrms.wumo.domain.location.dto.response.LocationGetAllResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationGetResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationRegisterResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationSpendingUpdateResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationUpdateResponse;
import org.prgrms.wumo.domain.location.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
@Tag(name = "후보장소 API")
public class LocationController {
	private final LocationService locationService;

	@PostMapping
	@Operation(summary = "후보장소 등록")
	public ResponseEntity<LocationRegisterResponse> registerLocation(
			@RequestBody @Valid LocationRegisterRequest locationRegisterRequest) {

		return new ResponseEntity<>(locationService.registerLocation(locationRegisterRequest), HttpStatus.CREATED);
	}

	@GetMapping("/{locationId}")
	@Operation(summary = "후보장소 상세 조회")
	public ResponseEntity<LocationGetResponse> getLocation(
			@PathVariable @Parameter(description = "조회할 후보장소 아이디") Long locationId) {

		return ResponseEntity.ok(locationService.getLocation(locationId));
	}

	@GetMapping
	@Operation(summary = "후보장소 목록 조회")
	public ResponseEntity<LocationGetAllResponse> getAllLocation(
			@Valid LocationGetAllRequest locationGetAllRequest) {
		return ResponseEntity.ok(locationService.getAllLocation(locationGetAllRequest));
	}

	@PatchMapping("/spending")
	@Operation(summary = "후보장소 사용 금액 갱신")
	public ResponseEntity<LocationSpendingUpdateResponse> updateSpending(
			@RequestBody @Valid LocationSpendingUpdateRequest locationSpendingUpdateRequest
	) {
		return ResponseEntity.ok(null);
	}

	@PatchMapping
	@Operation(summary = "후보장소 수정")
	public ResponseEntity<LocationUpdateResponse> updateLocation(
			@RequestBody @Valid LocationUpdateRequest locationUpdateRequest) {

		return ResponseEntity.ok(locationService.updateLocation(locationUpdateRequest));
	}

	@DeleteMapping("/{locationId}")
	@Operation(summary = "후보장소 삭제")
	public ResponseEntity<Void> deleteLocation(
			@PathVariable @Parameter(description = "삭제할 후보장소 아이디") Long locationId) {

		locationService.deleteLocation(locationId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping
	@Operation(summary = "루트에서 후보지 삭제")
	public ResponseEntity<Void> deleteRouteLocation(
			@RequestParam @Parameter(description = "루트에서 삭제할 후보지 식별자") long locationId) {

		locationService.deleteRouteLocation(locationId);
		return ResponseEntity.ok().build();
	}

}
