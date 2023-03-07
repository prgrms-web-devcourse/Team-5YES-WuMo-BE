package org.prgrms.wumo.domain.comment.service;

import static org.prgrms.wumo.global.jwt.JwtUtil.getMemberId;
import static org.prgrms.wumo.global.mapper.CommentMapper.toLocationComment;
import static org.prgrms.wumo.global.mapper.CommentMapper.toLocationCommentGetAllResponse;
import static org.prgrms.wumo.global.mapper.CommentMapper.toLocationCommentRegisterResponse;
import static org.prgrms.wumo.global.mapper.CommentMapper.toLocationCommentUpdateResponse;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.comment.dto.request.LocationCommentGetAllRequest;
import org.prgrms.wumo.domain.comment.dto.request.LocationCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.request.LocationCommentUpdateRequest;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentGetAllResponse;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentUpdateResponse;
import org.prgrms.wumo.domain.comment.model.LocationComment;
import org.prgrms.wumo.domain.comment.repository.LocationCommentRepository;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationCommentService {
	private final LocationCommentRepository locationCommentRepository;
	private final PartyMemberRepository partyMemberRepository;

	@Transactional
	public LocationCommentRegisterResponse registerLocationComment(
			LocationCommentRegisterRequest locationCommentRegisterRequest) {

		PartyMember partyMember = getPartyMemberEntity(locationCommentRegisterRequest.partyId(), getMemberId());

		LocationComment locationComment = toLocationComment(locationCommentRegisterRequest, partyMember);

		locationComment.setPartyMember(partyMember);
		locationComment.setMember(partyMember.getMember());

		return toLocationCommentRegisterResponse(
				locationCommentRepository.save(locationComment)
		);
	}

	@Transactional(readOnly = true)
	public LocationCommentGetAllResponse getAllLocationComments(
			LocationCommentGetAllRequest locationCommentGetAllRequest) {
		List<LocationComment> locationComments =
				locationCommentRepository.findAllByLocationId(locationCommentGetAllRequest.locationId(),
						locationCommentGetAllRequest.cursorId(), locationCommentGetAllRequest.pageSize());

		long lastId = (locationComments.size()) > 0 ?
				locationComments.get(locationComments.size() - 1).getId() : -1L;

		return toLocationCommentGetAllResponse(locationComments, lastId);
	}

	@Transactional
	public LocationCommentUpdateResponse updateLocationComment(
			LocationCommentUpdateRequest locationCommentUpdateRequest) {
		LocationComment locationComment = getLocationCommentEntity(locationCommentUpdateRequest.id());

		checkAuthorization(locationComment, getMemberId());

		locationComment.update(locationCommentUpdateRequest);

		return toLocationCommentUpdateResponse(locationCommentRepository.save(locationComment));
	}

	@Transactional
	public void deleteLocationComment(Long locationCommentId) {
		LocationComment locationComment = getLocationCommentEntity(locationCommentId);

		checkAuthorization(locationComment, getMemberId());

		locationCommentRepository.deleteById(locationCommentId);
	}

	private void checkMemberInParty(Long partyMemberId) {
		if (!partyMemberRepository.existsById(partyMemberId)) {
			throw new AccessDeniedException("모임 내 회원이 아닙니다.");
		}
	}

	private PartyMember getPartyMemberEntity(Long partyId, Long memberId) {
		return partyMemberRepository.findByPartyIdAndMemberId(partyId, memberId)
				.orElseThrow(() -> new EntityNotFoundException("모임 내 존재하지 않는 회원입니다."));
	}

	private LocationComment getLocationCommentEntity(Long locationCommentId) {
		return locationCommentRepository.findById(locationCommentId)
				.orElseThrow(() -> new EntityNotFoundException("존재 하지 않는 후보지 댓글입니다."));
	}

	private void checkAuthorization(LocationComment locationComment, Long memberId) {
		checkMemberInParty(locationComment.getPartyMember().getId());
		locationComment.checkAuthorization(memberId);
	}
}

