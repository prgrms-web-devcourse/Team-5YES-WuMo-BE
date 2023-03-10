package org.prgrms.wumo.domain.party.api;

import javax.validation.Valid;

import org.prgrms.wumo.domain.party.dto.request.PartyMemberGetRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyMemberRegisterRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyMemberUpdateRequest;
import org.prgrms.wumo.domain.party.dto.response.PartyMemberGetAllResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyMemberGetResponse;
import org.prgrms.wumo.domain.party.service.PartyMemberService;
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
@RequestMapping("/api/v1/parties")
@Tag(name = "모임 구성원 API")
public class PartyMemberController {

	private final PartyMemberService partyMemberService;

	@PostMapping("/{partyId}/members")
	@Operation(summary = "모임 구성원 등록")
	public ResponseEntity<Void> registerPartyMember(
			@PathVariable @Parameter(description = "모임 식별자", required = true) Long partyId,
			@RequestBody @Valid PartyMemberRegisterRequest partyMemberRegisterRequest
	) {
		partyMemberService.registerPartyMember(partyId, partyMemberRegisterRequest);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/{partyId}/members")
	@Operation(summary = "모임 구성원 목록 조회")
	public ResponseEntity<PartyMemberGetAllResponse> getAllPartyMembers(
			@PathVariable @Parameter(description = "모임 식별자", required = true) Long partyId,
			@Valid PartyMemberGetRequest partyMemberGetRequest
	) {
		return ResponseEntity.ok(partyMemberService.getAllPartyMembers(partyId, partyMemberGetRequest));
	}

	@GetMapping("/{partyId}/members/me")
	@Operation(summary = "모임 내 개인정보 조회")
	public ResponseEntity<PartyMemberGetResponse> getPartyMember(
			@PathVariable @Parameter(description = "모임 식별자", required = true) Long partyId
	) {
		return ResponseEntity.ok(partyMemberService.getPartyMember(partyId));
	}

	@PatchMapping("/{partyId}/members")
	@Operation(summary = "모임 구성원 정보 수정")
	public ResponseEntity<PartyMemberGetResponse> updatePartyMember(
			@PathVariable @Parameter(description = "모임 식별자", required = true) Long partyId,
			@RequestBody @Valid PartyMemberUpdateRequest partyMemberUpdateRequest
	) {
		return ResponseEntity.ok(partyMemberService.updatePartyMember(partyId, partyMemberUpdateRequest));
	}

	@DeleteMapping("/{partyId}/members")
	@Operation(summary = "모임 구성원 삭제 (모임 탈퇴)")
	public ResponseEntity<Void> deletePartyMember(
			@PathVariable @Parameter(description = "모임 식별자", required = true) Long partyId
	) {
		partyMemberService.deletePartyMember(partyId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{partyId}/members/{memberId}")
	@Operation(summary = "모임 구성원 삭제 (모임 추방)")
	public ResponseEntity<Long> deletePartyMember(
			@PathVariable @Parameter(description = "모임 식별자", required = true) Long partyId,
			@PathVariable @Parameter(description = "추방할 사용자 식별자", required = true) Long memberId
	) {
		partyMemberService.deletePartyMember(partyId, memberId);
		return ResponseEntity.ok(memberId);
	}

}
