package org.prgrms.wumo.domain.party.api;

import javax.validation.Valid;

import org.prgrms.wumo.domain.party.dto.request.PartyMemberGetRequest;
import org.prgrms.wumo.domain.party.dto.response.PartyMemberGetAllResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/party")
@Tag(name = "모임 구성원 API")
public class PartyMemberController {

	@GetMapping("/{partyId}/members")
	@Operation(summary = "모임 구성원 조회")
	public ResponseEntity<PartyMemberGetAllResponse> getAllPartyMembers(
			@PathVariable
			@Parameter(description = "모임 식별자", required = true)
			Long partyId,

			@Valid
			PartyMemberGetRequest request
	) {
		return ResponseEntity.ok(null);
	}

	@DeleteMapping("/{partyId}/members/{memberId}")
	@Operation(summary = "모임 구성원 삭제(추방)")
	public ResponseEntity<Void> deletePartyMember(
			@PathVariable
			@Parameter(description = "모임 식별자", required = true)
			Long partyId,

			@PathVariable
			@Parameter(description = "사용자 식별자", required = true)
			Long memberId
	) {
		return ResponseEntity.ok(null);
	}

}
