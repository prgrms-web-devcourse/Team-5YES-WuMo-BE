package org.prgrms.wumo.domain.party.api;

import javax.validation.Valid;

import org.prgrms.wumo.domain.party.dto.request.PartyGetRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyRegisterRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyUpdateRequest;
import org.prgrms.wumo.domain.party.dto.response.PartyGetAllResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyGetDetailResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyRegisterResponse;
import org.prgrms.wumo.domain.party.service.PartyService;
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
@RequestMapping("/api/v1/party")
@Tag(name = "모임 API")
public class PartyController {

	private final PartyService partyService;

	@PostMapping
	@Operation(summary = "모임 등록")
	public ResponseEntity<PartyRegisterResponse> registerParty(
			@RequestBody @Valid PartyRegisterRequest partyRegisterRequest
	) {
		return new ResponseEntity<>(partyService.registerParty(partyRegisterRequest), HttpStatus.CREATED);
	}

	@GetMapping("/members/{memberId}")
	@Operation(summary = "사용자 기준 모임 목록 조회")
	public ResponseEntity<PartyGetAllResponse> getAllParty(
			@PathVariable @Parameter(description = "사용자 식별자", required = true) Long memberId,
			@Valid PartyGetRequest partyGetRequest
	) {
		return ResponseEntity.ok(partyService.getAllParty(memberId, partyGetRequest));
	}

	@GetMapping("/{partyId}")
	@Operation(summary = "모임 상세 조회")
	public ResponseEntity<PartyGetDetailResponse> getParty(
			@PathVariable @Parameter(description = "모임 식별자", required = true) Long partyId
	) {
		return ResponseEntity.ok(partyService.getParty(partyId));
	}

	@PatchMapping("/{partyId}")
	@Operation(summary = "모임 수정")
	public ResponseEntity<Void> updateParty(
			@PathVariable @Parameter(description = "모임 식별자", required = true) Long partyId,
			@RequestBody @Valid PartyUpdateRequest partyUpdateRequest
	) {
		return ResponseEntity.ok(null);
	}

	@DeleteMapping("/{partyId}")
	@Operation(summary = "모임 삭제")
	public ResponseEntity<Void> deleteParty(
			@PathVariable @Parameter(description = "모임 식별자", required = true) Long partyId
	) {
		partyService.deleteParty(partyId);
		return ResponseEntity.ok().build();
	}

}
