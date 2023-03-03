package org.prgrms.wumo.domain.party.api;

import javax.validation.Valid;

import org.prgrms.wumo.domain.party.dto.request.InvitationRegisterRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyJoinRequest;
import org.prgrms.wumo.domain.party.dto.response.InvitationRegisterResponse;
import org.prgrms.wumo.domain.party.service.InvitationService;
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
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/parties")
@Tag(name = "모임 초대 API")
public class InvitationController {

	private final InvitationService invitationService;

	@PostMapping("/{partyId}/invitations")
	@Operation(summary = "모임 초대코드 생성")
	public ResponseEntity<InvitationRegisterResponse> registerInvitation(
			@PathVariable @Parameter(description = "모임 식별자", required = true) Long partyId,
			@RequestBody @Valid InvitationRegisterRequest invitationRegisterRequest
	) {
		return new ResponseEntity<>(
				invitationService.registerInvitation(partyId, invitationRegisterRequest),
				HttpStatus.CREATED
		);
	}

	@PostMapping("/join/{code}")
	@Operation(summary = "모임 참여 요청")
	public ResponseEntity<Void> joinParty(
			@PathVariable @Parameter(description = "초대 코드", required = true) String code,
			@RequestBody @Valid PartyJoinRequest request
	) {
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}

}
