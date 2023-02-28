package org.prgrms.wumo.domain.party.service;

import static org.prgrms.wumo.global.mapper.PartyMapper.toPartyMember;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.dto.request.PartyMemberRegisterRequest;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.party.repository.PartyRepository;
import org.prgrms.wumo.global.exception.custom.DuplicateException;
import org.prgrms.wumo.global.jwt.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartyMemberService {

	private final MemberRepository memberRepository;

	private final PartyRepository partyRepository;

	private final PartyMemberRepository partyMemberRepository;

	@Transactional
	public void registerPartyMember(
			Long partyId,
			PartyMemberRegisterRequest partyMemberRegisterRequest
	) {
		Long memberId = JwtUtil.getMemberId();
		if (partyMemberRepository.existsByPartyIdAndMemberId(partyId, memberId)) {
			throw new DuplicateException("이미 모임에 참여한 회원입니다.");
		}

		PartyMember partyMember = toPartyMember(
				getMemberEntity(memberId),
				getPartyEntity(partyId),
				partyMemberRegisterRequest.role(),
				false
		);

		partyMemberRepository.save(partyMember);
	}

	private Party getPartyEntity(Long partyId) {
		return partyRepository.findById(partyId)
				.orElseThrow(() -> new EntityNotFoundException("일치하는 모임이 없습니다."));
	}

	private Member getMemberEntity(Long memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(() -> new EntityNotFoundException("일치하는 회원이 없습니다."));
	}

}
