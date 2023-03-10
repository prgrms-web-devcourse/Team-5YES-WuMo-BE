package org.prgrms.wumo.domain.party.service;

import static org.prgrms.wumo.global.exception.ExceptionMessage.ENTITY_NOT_FOUND;
import static org.prgrms.wumo.global.exception.ExceptionMessage.MEMBER;
import static org.prgrms.wumo.global.exception.ExceptionMessage.PARTY;
import static org.prgrms.wumo.domain.party.mapper.PartyMapper.toPartyMember;
import static org.prgrms.wumo.domain.party.mapper.PartyMapper.toPartyMemberGetAllResponse;
import static org.prgrms.wumo.domain.party.mapper.PartyMapper.toPartyMemberGetResponse;

import java.util.List;
import java.util.Objects;

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
import org.springframework.security.access.AccessDeniedException;
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
			throw new DuplicateException("?????? ????????? ????????? ???????????????.");
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
		PartyMember partyMember = getPartyMemberEntity(partyId, JwtUtil.getMemberId());

		long totalMembers = partyMemberRepository.countAllByParty(partyMember.getParty());

		List<PartyMember> partyMembers =
				partyMemberRepository.findAllByPartyId(partyId, partyMemberGetRequest.cursorId(), partyMemberGetRequest.pageSize());

		long lastId = (partyMembers.size() > 0) ? partyMembers.get(partyMembers.size()-1).getId() : -1L;

		return toPartyMemberGetAllResponse(totalMembers, partyMembers, lastId);
	}

	@Transactional(readOnly = true)
	public PartyMemberGetResponse getPartyMember(Long partyId) {
		return toPartyMemberGetResponse(getPartyMemberEntity(partyId, JwtUtil.getMemberId()));
	}

	@Transactional
	public PartyMemberGetResponse updatePartyMember(Long partyId, PartyMemberUpdateRequest partyMemberUpdateRequest) {
		PartyMember partyMember = getPartyMemberEntity(partyId, JwtUtil.getMemberId());
		partyMember.updateRole(partyMemberUpdateRequest.role());

		return toPartyMemberGetResponse(partyMemberRepository.save(partyMember));
	}

	@Transactional
	public void deletePartyMember(Long partyId) {
		PartyMember partyMember = getPartyMemberEntity(partyId, JwtUtil.getMemberId());

		// ???????????? ?????? ?????????
		if (partyMember.isLeader()) {
			throw new AccessDeniedException("????????? ????????? ????????? ?????? ?????? ????????? ???????????? ?????????.");
		}

		partyMemberRepository.delete(partyMember);
	}

	@Transactional
	public void deletePartyMember(Long partyId, Long memberId) {
		if (Objects.equals(JwtUtil.getMemberId(), memberId)) {
			deletePartyMember(partyId);
			return;
		}

		PartyMember partyMember = getPartyMemberEntity(partyId, JwtUtil.getMemberId());
		if (partyMember.isLeader()) {
			partyMemberRepository.delete(getPartyMemberEntity(partyId, memberId));
		} else {
			throw new AccessDeniedException("????????? ????????? ????????? ????????? ????????? ???????????????.");
		}
	}

	private Party getPartyEntity(Long partyId) {
		return partyRepository.findById(partyId)
				.orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND.name(), PARTY.name())));
	}

	private Member getMemberEntity(Long memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND.name(), MEMBER.name())));
	}

	private PartyMember getPartyMemberEntity(Long partyId, Long memberId) {
		return partyMemberRepository.findByPartyIdAndMemberId(partyId, memberId)
				.orElseThrow(() -> new EntityNotFoundException("?????? ????????? ????????? ????????? ????????????."));
	}

}
