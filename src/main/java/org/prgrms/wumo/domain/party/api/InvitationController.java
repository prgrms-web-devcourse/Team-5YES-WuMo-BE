package org.prgrms.wumo.domain.party.api;

import javax.validation.Valid;

import org.prgrms.wumo.domain.party.dto.request.InvitationRegisterRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyJoinRequest;
import org.prgrms.wumo.domain.party.dto.response.InvitationRegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/party")
@Tag(name = "모임 초대 API")
public class InvitationController {

	@PostMapping("/{partyId}/invitation")
	@Operation(summary = "모임 초대 코드 생성")
	public ResponseEntity<InvitationRegisterResponse> registerInvitation(
			@PathVariable
			@Parameter(description = "파티 식별자", required = true) long partyId,

			@RequestBody @Valid
			InvitationRegisterRequest request
	) {
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}

	@PostMapping("/join/{code}")
	@Operation(summary = "모임 참여 요청")
	public ResponseEntity<Void> joinParty(
			@PathVariable
			@Parameter(description = "초대 코드", required = true) String code,

			@RequestBody @Valid
			PartyJoinRequest request
	) {
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}

}
