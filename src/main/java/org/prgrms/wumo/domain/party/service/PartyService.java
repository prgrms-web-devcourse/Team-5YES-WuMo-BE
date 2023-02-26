package org.prgrms.wumo.domain.party.service;

import static org.prgrms.wumo.global.mapper.PartyMapper.toParty;
import static org.prgrms.wumo.global.mapper.PartyMapper.toPartyRegisterResponse;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.dto.request.PartyRegisterRequest;
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
		Member member = memberRepository.findById(partyRegisterRequest.memberId())
				.orElseThrow(() -> new EntityNotFoundException("사용자 아이디에 문제가 발생했습니다."));
		PartyMember partyLeader = PartyMember.builder()
				.member(member)
				.party(party)
				.role(partyRegisterRequest.role())
				.isLeader(true)
				.build();
		partyMemberRepository.save(partyLeader);

		return toPartyRegisterResponse(party);
	}

}
