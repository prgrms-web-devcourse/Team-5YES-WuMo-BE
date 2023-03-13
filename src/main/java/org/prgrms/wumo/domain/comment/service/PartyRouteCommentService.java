package org.prgrms.wumo.domain.comment.service;

import static org.prgrms.wumo.global.exception.ExceptionMessage.COMMENT;
import static org.prgrms.wumo.global.exception.ExceptionMessage.ENTITY_NOT_FOUND;
import static org.prgrms.wumo.global.exception.ExceptionMessage.LOCATION;
import static org.prgrms.wumo.global.exception.ExceptionMessage.PARTY_MEMBER;
import static org.prgrms.wumo.global.exception.ExceptionMessage.WRONG_ACCESS;
import static org.prgrms.wumo.global.jwt.JwtUtil.getMemberId;
import static org.prgrms.wumo.domain.comment.mapper.CommentMapper.toPartyRouteComment;
import static org.prgrms.wumo.domain.comment.mapper.CommentMapper.toPartyRouteCommentGetAllResponse;
import static org.prgrms.wumo.domain.comment.mapper.CommentMapper.toPartyRouteCommentRegisterResponse;
import static org.prgrms.wumo.domain.comment.mapper.CommentMapper.toPartyRouteCommentUpdateResponse;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentGetAllRequest;
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentUpdateRequest;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentGetAllResponse;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentUpdateResponse;
import org.prgrms.wumo.domain.comment.model.PartyRouteComment;
import org.prgrms.wumo.domain.comment.repository.PartyRouteCommentRepository;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.route.model.Route;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartyRouteCommentService {

	private final PartyRouteCommentRepository partyRouteCommentRepository;
	private final PartyMemberRepository partyMemberRepository;
	private final LocationRepository locationRepository;

	@Transactional
	public PartyRouteCommentRegisterResponse registerPartyRouteComment(
			PartyRouteCommentRegisterRequest partyRouteCommentRegisterRequest) {

		PartyMember partyMember = getPartyMemberEntity(partyRouteCommentRegisterRequest.partyId(), getMemberId());
		Location location = getLocationEntity(partyRouteCommentRegisterRequest.locationId());
		Route route = location.getRoute();
		PartyRouteComment partyRouteComment = toPartyRouteComment(partyRouteCommentRegisterRequest,
				route.getId());

		partyRouteComment.setPartyMember(partyMember);
		partyRouteComment.setMember(partyMember.getMember());

		return toPartyRouteCommentRegisterResponse(
				partyRouteCommentRepository.save(partyRouteComment)
		);
	}

	@Transactional(readOnly = true)
	public PartyRouteCommentGetAllResponse getAllPartyRouteComment(
			PartyRouteCommentGetAllRequest partyRouteCommentGetAllRequest) {
		List<PartyRouteComment> partyRouteComments =
				partyRouteCommentRepository.findAllByLocationId(partyRouteCommentGetAllRequest.cursorId(),
						partyRouteCommentGetAllRequest.pageSize(), partyRouteCommentGetAllRequest.locationId());

		List<Boolean> isEditables = partyRouteComments.stream()
				.map(partyRouteComment -> getIsEditable(partyRouteComment, getMemberId()))
				.toList();

		long lastId = (partyRouteComments.size()) > 0 ?
				partyRouteComments.get(partyRouteComments.size() - 1).getId() : -1L;

		return toPartyRouteCommentGetAllResponse(partyRouteComments, isEditables, lastId);
	}

	@Transactional
	public PartyRouteCommentUpdateResponse updatePartyRouteComment(
			PartyRouteCommentUpdateRequest partyRouteCommentUpdateRequest) {
		PartyRouteComment partyRouteComment = getPartyRouteCommentEntity(partyRouteCommentUpdateRequest.id());

		checkAuthorization(partyRouteComment, getMemberId());

		partyRouteComment.update(partyRouteCommentUpdateRequest);

		return toPartyRouteCommentUpdateResponse(partyRouteCommentRepository.save(partyRouteComment));
	}

	@Transactional
	public void deletePartyRouteComment(Long partyRouteCommentId) {
		PartyRouteComment partyRouteComment = getPartyRouteCommentEntity(partyRouteCommentId);

		checkAuthorization(partyRouteComment, getMemberId());

		partyRouteCommentRepository.deleteById(partyRouteCommentId);
	}

	private PartyMember getPartyMemberEntity(Long partyId, Long memberId) {
		return partyMemberRepository.findByPartyIdAndMemberId(partyId, memberId)
				.orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND.name(), PARTY_MEMBER.name())));
	}

	private PartyRouteComment getPartyRouteCommentEntity(Long partyRouteCommentId) {
		return partyRouteCommentRepository.findById(partyRouteCommentId)
				.orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND.name(), COMMENT.name())));
	}

	private Location getLocationEntity(Long locationId) {
		return locationRepository.findById(locationId)
				.orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND.name(), LOCATION.name())));
	}

	private void checkMemberInParty(Long partyMemberId) {
		if (!partyMemberRepository.existsById(partyMemberId)) {
			throw new AccessDeniedException(WRONG_ACCESS.name());
		}
	}

	private void checkAuthorization(PartyRouteComment partyRouteComment, Long memberId) {
		checkMemberInParty(partyRouteComment.getPartyMember().getId());
		partyRouteComment.checkAuthorization(memberId);
	}

	private boolean getIsEditable(PartyRouteComment partyRouteComment, Long memberId) {
		return Objects.equals(partyRouteComment.getMember().getId(), memberId);
	}
}

