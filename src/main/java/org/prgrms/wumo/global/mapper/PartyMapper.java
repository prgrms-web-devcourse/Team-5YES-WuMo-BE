package org.prgrms.wumo.global.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.party.dto.request.PartyRegisterRequest;
import org.prgrms.wumo.domain.party.dto.response.InvitationRegisterResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyGetAllResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyGetResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyMemberGetAllResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyMemberGetResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyRegisterResponse;
import org.prgrms.wumo.domain.party.model.Invitation;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.global.base62.Base62Util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PartyMapper {

	public static Party toParty(PartyRegisterRequest partyRegisterRequest) {
		return Party.builder()
				.name(partyRegisterRequest.name())
				.startDate(LocalDateTime.of(partyRegisterRequest.startDate(), LocalTime.MIN))
				.endDate(LocalDateTime.of(partyRegisterRequest.endDate(), LocalTime.MIN))
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
						LocalDate.from(party.getStartDate()),
						LocalDate.from(party.getEndDate()),
						party.getDescription(),
						party.getCoverImage()))
				.toList();
		return new PartyGetAllResponse(partyGetResponses, lastId);
	}

	public static PartyGetResponse toPartyGetDetailResponse(Party party) {
		return new PartyGetResponse(
				party.getId(),
				party.getName(),
				LocalDate.from(party.getStartDate()),
				LocalDate.from(party.getEndDate()),
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

	public static PartyMemberGetAllResponse toPartyMemberGetAllResponse(List<PartyMember> partyMembers, Long lastId) {
		List<PartyMemberGetResponse> partyMemberGetResponses = partyMembers.stream()
				.map(PartyMapper::toPartyMemberGetResponse)
				.toList();
		return new PartyMemberGetAllResponse(partyMemberGetResponses, lastId);
	}

	public static PartyMemberGetResponse toPartyMemberGetResponse(PartyMember partyMember) {
		return new PartyMemberGetResponse(
				partyMember.getMember().getId(),
				partyMember.getMember().getNickname(),
				partyMember.getRole(),
				partyMember.getMember().getProfileImage()
		);
	}

	public static InvitationRegisterResponse toInvitationRegisterResponse(Invitation invitation) {
		return new InvitationRegisterResponse(Base62Util.encode(invitation.getId()));
	}

}
