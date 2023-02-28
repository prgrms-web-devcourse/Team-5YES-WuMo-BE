package org.prgrms.wumo.domain.party.service;

import static org.prgrms.wumo.global.mapper.PartyMapper.toPartyMember;
import static org.prgrms.wumo.global.mapper.PartyMapper.toPartyMemberGetAllResponse;
import static org.prgrms.wumo.global.mapper.PartyMapper.toPartyMemberGetResponse;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.dto.request.PartyMemberGetRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyMemberRegisterRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyMemberUpdateRequest;
import org.prgrms.wumo.domain.party.dto.response.PartyMemberGetAllResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyMemberGetResponse;
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

	@Transactional(readOnly = true)
	public PartyMemberGetAllResponse getAllPartyMembers(Long partyId, PartyMemberGetRequest partyMemberGetRequest) {
		List<PartyMember> partyMembers =
				partyMemberRepository.findAllByPartyId(partyId, partyMemberGetRequest.cursorId(), partyMemberGetRequest.pageSize());

		long lastId = (partyMembers.size() > 0) ? partyMembers.get(partyMembers.size()-1).getId() : -1L;

		return toPartyMemberGetAllResponse(partyMembers, lastId);
	}

	@Transactional
	public PartyMemberGetResponse updatePartyMember(Long partyId, PartyMemberUpdateRequest partyMemberUpdateRequest) {
		PartyMember partyMember = getPartyMemberEntity(partyId, JwtUtil.getMemberId());
		partyMember.updateRole(partyMemberUpdateRequest.role());

		return toPartyMemberGetResponse(partyMemberRepository.save(partyMember));
	}

	private Party getPartyEntity(Long partyId) {
		return partyRepository.findById(partyId)
				.orElseThrow(() -> new EntityNotFoundException("일치하는 모임이 없습니다."));
	}

	private Member getMemberEntity(Long memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(() -> new EntityNotFoundException("일치하는 회원이 없습니다."));
	}

	private PartyMember getPartyMemberEntity(Long partyId, Long memberId) {
		return partyMemberRepository.findByPartyIdAndMemberId(partyId, memberId)
				.orElseThrow(() -> new EntityNotFoundException("해당 모임에 가입된 회원이 아닙니다."));
	}

}
