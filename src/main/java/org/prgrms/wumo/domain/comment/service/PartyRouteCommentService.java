package org.prgrms.wumo.domain.comment.service;

import static org.prgrms.wumo.global.jwt.JwtUtil.getMemberId;
import static org.prgrms.wumo.global.mapper.CommentMapper.toPartyRouteComment;
import static org.prgrms.wumo.global.mapper.CommentMapper.toPartyRouteCommentGetAllResponse;
import static org.prgrms.wumo.global.mapper.CommentMapper.toPartyRouteCommentRegisterResponse;
import static org.prgrms.wumo.global.mapper.CommentMapper.toPartyRouteCommentUpdateResponse;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentGetAllRequest;
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentUpdateRequest;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentGetAllResponse;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentUpdateResponse;
import org.prgrms.wumo.domain.comment.model.PartyRouteComment;
import org.prgrms.wumo.domain.comment.repository.PartyRouteCommentRepository;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.route.model.Route;
import org.prgrms.wumo.domain.route.repository.RouteRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartyRouteCommentService {

	private final PartyRouteCommentRepository partyRouteCommentRepository;
	private final MemberRepository memberRepository;
	private final PartyMemberRepository partyMemberRepository;
	private final RouteRepository routeRepository;
	private final LocationRepository locationRepository;

	@Transactional
	public PartyRouteCommentRegisterResponse registerPartyRouteComment(
			PartyRouteCommentRegisterRequest partyRouteCommentRegisterRequest) {

		PartyRouteComment partyRouteComment = toPartyRouteComment(partyRouteCommentRegisterRequest);

		partyRouteComment.setMember(getMemberEntity(getMemberId()));

		partyRouteComment.setPartyMember(
				getPartyMemberEntity(
						getRouteEntity(
								partyRouteCommentRegisterRequest.routeId()).getParty().getId(),
						getMemberId()
				)
		);

		validateLocation(partyRouteCommentRegisterRequest.locationId());

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

		long lastId = (partyRouteComments.size()) > 0 ?
				partyRouteComments.get(partyRouteComments.size() - 1).getId() : -1L;

		return toPartyRouteCommentGetAllResponse(partyRouteComments, lastId);
	}

	@Transactional
	public PartyRouteCommentUpdateResponse updatePartyRouteComment(
			PartyRouteCommentUpdateRequest partyRouteCommentUpdateRequest) {
		PartyRouteComment partyRouteComment = getPartyRouteCommentEntity(partyRouteCommentUpdateRequest.id());

		checkMemberInParty(partyRouteComment.getPartyMember().getId());

		if (!partyRouteComment.getMember().getId().equals(getMemberId())) {
			throw new AccessDeniedException("댓글은 작성자만 수정할 수 있습니다.");
		}
		partyRouteComment.update(partyRouteCommentUpdateRequest);

		return toPartyRouteCommentUpdateResponse(partyRouteCommentRepository.save(partyRouteComment));
	}

	@Transactional
	public void deletePartyRouteComment(Long partyRouteCommentId) {
		PartyRouteComment partyRouteComment = getPartyRouteCommentEntity(partyRouteCommentId);

		checkMemberInParty(partyRouteComment.getPartyMember().getId());

		if (!partyRouteComment.getMember().getId().equals(getMemberId())) {
			throw new AccessDeniedException("댓글은 작성자만 수정할 수 있습니다.");
		}

		partyRouteCommentRepository.deleteById(partyRouteCommentId);
	}

	private Member getMemberEntity(Long memberId) {
		return memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("존재 하지 않는 회원입니다"));
	}

	private Route getRouteEntity(Long routeId) {
		return routeRepository.findById(routeId).orElseThrow(() -> new EntityNotFoundException("루트가 존재하지 않습니다"));
	}

	private PartyMember getPartyMemberEntity(Long partyId, Long memberId) {
		return partyMemberRepository.findByPartyIdAndMemberId(partyId, memberId)
				.orElseThrow(() -> new EntityNotFoundException("모임 내 존재하지 않는 회원입니다"));
	}

	private void validateLocation(Long locationId) {
		if (!locationRepository.existsById(locationId)) {
			throw new EntityNotFoundException("댓글을 작성하고자 하는 후보지가 존재하지 않습니다");
		}
	}

	private PartyRouteComment getPartyRouteCommentEntity(Long partyRouteCommentId) {
		return partyRouteCommentRepository.findById(partyRouteCommentId)
				.orElseThrow(() -> new EntityNotFoundException("모임 내 존재하지 않는 회원입니다"));
	}

	private void checkMemberInParty(Long partyMemberId) {
		if (!partyMemberRepository.existsById(partyMemberId)) {
			throw new AccessDeniedException("모임 내 회원이 아닙니다.");
		}
	}
}
