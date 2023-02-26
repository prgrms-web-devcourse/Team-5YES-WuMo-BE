package org.prgrms.wumo.global.mapper;

import java.util.List;

import org.prgrms.wumo.domain.party.dto.request.PartyRegisterRequest;
import org.prgrms.wumo.domain.party.dto.response.PartyGetAllResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyGetDetailResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyGetResponse;
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

	public static PartyGetAllResponse toPartyGetAllResponse(List<Party> parties) {
		List<PartyGetResponse> partyGetResponses = parties.stream()
				.map(party -> new PartyGetResponse(party.getId(), party.getName(), party.getCoverImage()))
				.toList();
		return new PartyGetAllResponse(partyGetResponses);
	}

	public static PartyGetDetailResponse toPartyGetDetailResponse(Party party) {
		return new PartyGetDetailResponse(
				party.getId(),
				party.getName(),
				party.getStartDate(),
				party.getEndDate(),
				party.getDescription(),
				party.getCoverImage()
		);
	}

}
