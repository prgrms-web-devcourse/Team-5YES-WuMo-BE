package org.prgrms.wumo.domain.party.service;

import static org.prgrms.wumo.domain.party.mapper.PartyMapper.toInvitationRegisterResponse;
import static org.prgrms.wumo.domain.party.mapper.PartyMapper.toInvitationValidateResponse;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.party.dto.request.InvitationRegisterRequest;
import org.prgrms.wumo.domain.party.dto.response.InvitationRegisterResponse;
import org.prgrms.wumo.domain.party.dto.response.InvitationValidateResponse;
import org.prgrms.wumo.domain.party.model.Invitation;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.InvitationRepository;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.global.base62.Base62Util;
import org.prgrms.wumo.global.exception.custom.ExpiredInvitationException;
import org.prgrms.wumo.global.jwt.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvitationService {

	private final PartyMemberRepository partyMemberRepository;

	private final InvitationRepository invitationRepository;

	@Transactional
	public InvitationRegisterResponse registerInvitation(
			Long partyId,
			InvitationRegisterRequest invitationRegisterRequest
	) {
		PartyMember partyMember = getPartyMemberEntity(partyId, JwtUtil.getMemberId());

		Optional<Invitation> optionalInvitation = invitationRepository.findTopByPartyOrderByIdDesc(partyMember.getParty());
		if (optionalInvitation.isPresent()) {
			// 이미 생성한 초대장이 만료되지 않은 경우 기존 초대장 제공
			Invitation oldInvitation = optionalInvitation.get();
			if (oldInvitation.getExpiredDate().isAfter(LocalDateTime.now())) {
				return toInvitationRegisterResponse(oldInvitation);
			}
		}

		// 초대장을 발급한 적이 없거나 기존 초대장 만료된 경우 신규 초대장 생성
		Invitation newInvitation = Invitation.builder()
				.party(partyMember.getParty())
				.expiredDate(LocalDateTime.of(invitationRegisterRequest.expiredDate(), LocalTime.MAX))
				.build();

		return toInvitationRegisterResponse(invitationRepository.save(newInvitation));
	}

	@Transactional(readOnly = true)
	public InvitationValidateResponse validateInvitation(String code) {
		Invitation invitation = getInvitationEntity(code);
		if (invitation.getExpiredDate().isBefore(LocalDateTime.now())) {
			throw new ExpiredInvitationException("유효기간이 만료된 초대코드입니다.");
		}

		return toInvitationValidateResponse(invitation);
	}

	private PartyMember getPartyMemberEntity(Long partyId, Long memberId) {
		return partyMemberRepository.findByPartyIdAndMemberId(partyId, memberId)
				.orElseThrow(() -> new EntityNotFoundException("해당 모임에 가입된 회원이 아닙니다."));
	}

	private Invitation getInvitationEntity(String code) {
		return invitationRepository.findById(Base62Util.decode(code))
				.orElseThrow(() -> new EntityNotFoundException("올바르지 않은 초대코드입니다."));
	}

}
