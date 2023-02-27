package org.prgrms.wumo.domain.party.service;

import static org.prgrms.wumo.global.mapper.PartyMapper.toParty;
import static org.prgrms.wumo.global.mapper.PartyMapper.toPartyGetAllResponse;
import static org.prgrms.wumo.global.mapper.PartyMapper.toPartyGetDetailResponse;
import static org.prgrms.wumo.global.mapper.PartyMapper.toPartyRegisterResponse;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.dto.request.PartyGetRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyRegisterRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyUpdateRequest;
import org.prgrms.wumo.domain.party.dto.response.PartyGetAllResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyGetDetailResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyRegisterResponse;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.party.repository.PartyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartyService {

	private final MemberRepository memberRepository;

	private final PartyRepository partyRepository;

	private final PartyMemberRepository partyMemberRepository;

	@Transactional
	public PartyRegisterResponse registerParty(PartyRegisterRequest partyRegisterRequest) {
		// 모임 엔티티 저장
		Party party = toParty(partyRegisterRequest);
		party = partyRepository.save(party);

		// 모임장 저장
		Member member = getMemberEntity(partyRegisterRequest.memberId());
		PartyMember partyLeader = PartyMember.builder()
				.member(member)
				.party(party)
				.role(partyRegisterRequest.role())
				.isLeader(true)
				.build();
		partyMemberRepository.save(partyLeader);

		return toPartyRegisterResponse(party);
	}

	@Transactional(readOnly = true)
	public PartyGetAllResponse getAllParty(Long memberId, PartyGetRequest partyGetRequest) {
		List<Party> parties = partyMemberRepository.findAllByMember(getMemberEntity(memberId)).stream()
				.map(PartyMember::getParty)
				.toList();

		return toPartyGetAllResponse(parties);
	}

	@Transactional(readOnly = true)
	public PartyGetDetailResponse getParty(Long partyId) {
		return toPartyGetDetailResponse(getPartyEntity(partyId));
	}

	@Transactional
	public PartyGetDetailResponse updateParty(Long partyId, PartyUpdateRequest partyUpdateRequest) {
		Party party = getPartyEntity(partyId);
		party.update(
				partyUpdateRequest.name(),
				partyUpdateRequest.startDate(),
				partyUpdateRequest.endDate(),
				partyUpdateRequest.description(),
				partyUpdateRequest.coverImage(),
				partyUpdateRequest.password()
		);

		return toPartyGetDetailResponse(partyRepository.save(party));
	}

	@Transactional
	public void deleteParty(Long partyId) {
		Party party = getPartyEntity(partyId);
		partyMemberRepository.deleteAllByParty(party);
		partyRepository.delete(party);
	}

	private Member getMemberEntity(Long memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(() -> new EntityNotFoundException("사용자 아이디에 문제가 발생했습니다."));
	}

	private Party getPartyEntity(Long partyId) {
		return partyRepository.findById(partyId)
				.orElseThrow(() -> new EntityNotFoundException("일치하는 모임이 없습니다."));
	}

}
