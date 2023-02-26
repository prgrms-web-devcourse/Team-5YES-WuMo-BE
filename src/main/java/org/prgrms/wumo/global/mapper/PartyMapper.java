package org.prgrms.wumo.global.mapper;

import org.prgrms.wumo.domain.party.dto.request.PartyRegisterRequest;
import org.prgrms.wumo.domain.party.dto.response.PartyRegisterResponse;
import org.prgrms.wumo.domain.party.model.Party;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PartyMapper {

	public static Party toParty(PartyRegisterRequest partyRegisterRequest) {
		return Party.builder()
				.name(partyRegisterRequest.name())
				.startDate(partyRegisterRequest.startDate())
				.endDate(partyRegisterRequest.endDate())
				.description(partyRegisterRequest.description())
				.coverImage(partyRegisterRequest.coverImage())
				.password(partyRegisterRequest.password())
				.build();
	}

	public static PartyRegisterResponse toPartyRegisterResponse(Party party) {
		return new PartyRegisterResponse(party.getId());
	}

}
