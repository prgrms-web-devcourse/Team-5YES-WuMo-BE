package org.prgrms.wumo.domain.comment.service;

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
				.orElseThrow(() -> new EntityNotFoundException("모임 내 존재하지 않는 회원입니다"));
	}

	private PartyRouteComment getPartyRouteCommentEntity(Long partyRouteCommentId) {
		return partyRouteCommentRepository.findById(partyRouteCommentId)
				.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 모임 내 댓글입니다."));
	}

	private Location getLocationEntity(Long locationId) {
		return locationRepository.findById(locationId)
				.orElseThrow(() -> new EntityNotFoundException("댓글을 작성하고자 하는 후보지가 존재하지 않습니다"));
	}

	private void checkMemberInParty(Long partyMemberId) {
		if (!partyMemberRepository.existsById(partyMemberId)) {
			throw new AccessDeniedException("모임 내 회원이 아닙니다.");
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

