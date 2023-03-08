package org.prgrms.wumo.domain.party.service;

import static org.prgrms.wumo.global.mapper.PartyMapper.toParty;
import static org.prgrms.wumo.global.mapper.PartyMapper.toPartyGetAllResponse;
import static org.prgrms.wumo.global.mapper.PartyMapper.toPartyGetDetailResponse;
import static org.prgrms.wumo.global.mapper.PartyMapper.toPartyRegisterResponse;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.image.repository.ImageRepository;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.dto.request.PartyGetRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyRegisterRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyUpdateRequest;
import org.prgrms.wumo.domain.party.dto.response.PartyGetAllResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyGetResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyRegisterResponse;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.InvitationRepository;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.party.repository.PartyRepository;
import org.prgrms.wumo.global.exception.custom.PartyNotEmptyException;
import org.prgrms.wumo.global.jwt.JwtUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartyService {

	private static final int MEMBER_PREVIEW = 3;

	private final MemberRepository memberRepository;

	private final PartyRepository partyRepository;

	private final PartyMemberRepository partyMemberRepository;

	private final InvitationRepository invitationRepository;

	private final ImageRepository imageRepository;

	@Transactional
	public PartyRegisterResponse registerParty(PartyRegisterRequest partyRegisterRequest) {
		// 모임 엔티티 저장
		Party party = toParty(partyRegisterRequest);
		party = partyRepository.save(party);

		// 모임장 저장
		Member member = getMemberEntity(JwtUtil.getMemberId());
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
	public PartyGetAllResponse getAllParty(PartyGetRequest partyGetRequest) {
		List<PartyMember> partyMembers = partyMemberRepository.findAllByMemberId(
				JwtUtil.getMemberId(),
				partyGetRequest.cursorId(),
				partyGetRequest.pageSize(),
				partyGetRequest.partyType()
		);

		long lastId = (partyMembers.size() > 0) ? partyMembers.get(partyMembers.size() - 1).getId() : -1L;

		List<Party> parties = partyMembers.stream()
				.map(PartyMember::getParty)
				.peek(this::setPartyMemberDetail)
				.toList();

		return toPartyGetAllResponse(parties, lastId);
	}

	@Transactional(readOnly = true)
	public PartyGetResponse getParty(Long partyId) {
		Party party = getPartyEntity(partyId);
		setPartyMemberDetail(party);

		return toPartyGetDetailResponse(party);
	}

	@Transactional
	public PartyGetResponse updateParty(Long partyId, PartyUpdateRequest partyUpdateRequest) {
		PartyMember partyLeader = getPartyLeaderEntity(partyId);
		checkAuthorization(partyLeader);

		Party party = partyLeader.getParty();
		party.update(
				partyUpdateRequest.name(),
				LocalDateTime.of(partyUpdateRequest.startDate(), LocalTime.MIN),
				LocalDateTime.of(partyUpdateRequest.endDate(), LocalTime.MAX),
				partyUpdateRequest.description(),
				partyUpdateRequest.coverImage()
		);
		setPartyMemberDetail(party);

		return toPartyGetDetailResponse(partyRepository.save(party));
	}

	@Transactional
	public void deleteParty(Long partyId) {
		PartyMember partyLeader = getPartyLeaderEntity(partyId);
		checkAuthorization(partyLeader);

		// 모임장인 경우 모임에 멤버가 본인을 제외하고 없어야만 삭제 가능
		List<PartyMember> partyMembers = partyMemberRepository.findAllByPartyId(partyId, null, 2);
		if (partyMembers.size() == 1 && Objects.equals(partyMembers.get(0).getId(), partyLeader.getId())) {
			imageRepository.delete(partyLeader.getParty().getCoverImage());
			invitationRepository.deleteAllByParty(partyLeader.getParty());
			partyMemberRepository.delete(partyLeader);
			partyRepository.deleteById(partyId);
		} else {
			throw new PartyNotEmptyException("본인을 제외하고 모임에 가입된 회원이 없어야 합니다.");
		}
	}

	private Member getMemberEntity(Long memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(() -> new EntityNotFoundException("일치하는 회원이 없습니다."));
	}

	private Party getPartyEntity(Long partyId) {
		return partyRepository.findById(partyId)
				.orElseThrow(() -> new EntityNotFoundException("일치하는 모임이 없습니다."));
	}

	private PartyMember getPartyLeaderEntity(Long partyId) {
		return partyMemberRepository.findByPartyIdAndIsLeader(partyId)
				.orElseThrow(() -> new EntityNotFoundException("일치하는 모임이 없습니다."));
	}

	private void checkAuthorization(PartyMember partyMember) {
		if (!JwtUtil.isValidAccess(partyMember.getMember().getId())) {
			throw new AccessDeniedException("모임을 생성한 회원만 가능합니다.");
		}
	}

	private void setPartyMemberDetail(Party party) {
		// TODO : N개의 Party에 대해 2번씩 쿼리가 나가기 때문에 개선할 필요
		party.setTotalMembers(partyMemberRepository.countAllByParty(party));
		party.setPartyMembers(partyMemberRepository.findAllByPartyId(party.getId(), null, MEMBER_PREVIEW));
	}

}
