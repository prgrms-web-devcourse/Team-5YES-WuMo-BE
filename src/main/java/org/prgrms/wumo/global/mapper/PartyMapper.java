package org.prgrms.wumo.global.mapper;

import java.util.List;

import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.party.dto.request.PartyRegisterRequest;
import org.prgrms.wumo.domain.party.dto.response.PartyGetAllResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyGetResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyRegisterResponse;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;

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

	public static PartyGetAllResponse toPartyGetAllResponse(List<Party> parties, Long lastId) {
		List<PartyGetResponse> partyGetResponses = parties.stream()
				.map(party -> new PartyGetResponse(
						party.getId(),
						party.getName(),
						party.getStartDate(),
						party.getEndDate(),
						party.getDescription(),
						party.getCoverImage()))
				.toList();
		return new PartyGetAllResponse(partyGetResponses, lastId);
	}

	public static PartyGetResponse toPartyGetDetailResponse(Party party) {
		return new PartyGetResponse(
				party.getId(),
				party.getName(),
				party.getStartDate(),
				party.getEndDate(),
				party.getDescription(),
				party.getCoverImage()
		);
	}

	public static PartyMember toPartyMember(Member member, Party party, String role, boolean isLeader) {
		return PartyMember.builder()
				.member(member)
				.party(party)
				.role(role)
				.isLeader(false)
				.build();
	}

}
