package org.prgrms.wumo.domain.party.service;

import static org.prgrms.wumo.global.exception.ExceptionMessage.ENTITY_NOT_FOUND;
import static org.prgrms.wumo.global.exception.ExceptionMessage.MEMBER;
import static org.prgrms.wumo.global.exception.ExceptionMessage.PARTY;
import static org.prgrms.wumo.global.mapper.PartyMapper.toParty;
import static org.prgrms.wumo.global.mapper.PartyMapper.toPartyGetAllResponse;
import static org.prgrms.wumo.global.mapper.PartyMapper.toPartyGetDetailResponse;
import static org.prgrms.wumo.global.mapper.PartyMapper.toPartyMember;
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
		PartyMember partyLeader = toPartyMember(member, party, partyRegisterRequest.role(), true);
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
		setPartyDetail(partyMembers);

		long lastId = (partyMembers.size() > 0) ? partyMembers.get(partyMembers.size() - 1).getId() : -1L;

		List<Party> parties = partyMembers.stream()
				.map(PartyMember::getParty)
				.toList();

		return toPartyGetAllResponse(parties, lastId);
	}

	@Transactional(readOnly = true)
	public PartyGetResponse getParty(Long partyId) {
		PartyMember partyMember = getPartyMemberEntity(partyId, JwtUtil.getMemberId());
		setPartyDetail(partyMember.getParty());

		return toPartyGetDetailResponse(partyMember.getParty());
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
		setPartyDetail(party);

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
				.orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND.name(), MEMBER.name())));
	}

	private PartyMember getPartyLeaderEntity(Long partyId) {
		return partyMemberRepository.findByPartyIdAndIsLeader(partyId)
				.orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND.name(), PARTY.name())));
	}

	private PartyMember getPartyMemberEntity(Long partyId, Long memberId) {
		return partyMemberRepository.findByPartyIdAndMemberId(partyId, memberId)
				.orElseThrow(() -> new EntityNotFoundException("해당 모임에 가입된 회원이 아닙니다."));
	}

	private void checkAuthorization(PartyMember partyMember) {
		if (!JwtUtil.isValidAccess(partyMember.getMember().getId())) {
			throw new AccessDeniedException("모임을 생성한 회원만 가능합니다.");
		}
	}

	// 모임 다건에 대해 세부 정보를 설정
	private void setPartyDetail(List<PartyMember> partyMembers) {
		List<Long> partyIds = partyMembers.stream()
				.map(partyMember -> partyMember.getParty().getId())
				.toList();
		List<Long> totalMembers = partyMemberRepository.countAllByPartyIdIn(partyIds);
		List<PartyMember> partyLeaders = partyMemberRepository.findAllByPartyIdInAndIsLeader(partyIds);

		for (int i = 0; i < partyMembers.size(); i++) {
			Party party = partyMembers.get(i).getParty();
			party.setTotalMembers(totalMembers.get(i));
			party.setPartyMembers(List.of(partyLeaders.get(i)));
		}
	}

	// 모임 단건에 대해 세부 정보를 설정
	private void setPartyDetail(Party party) {
		party.setTotalMembers(partyMemberRepository.countAllByParty(party));
		party.setPartyMembers(partyMemberRepository.findAllByPartyIdInAndIsLeader(List.of(party.getId())));
	}

}
