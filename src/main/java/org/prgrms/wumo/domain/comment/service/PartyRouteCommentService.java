package org.prgrms.wumo.domain.comment.service;

import static org.prgrms.wumo.global.jwt.JwtUtil.getMemberId;
import static org.prgrms.wumo.global.mapper.CommentMapper.toPartyRouteComment;
import static org.prgrms.wumo.global.mapper.CommentMapper.toPartyRouteCommentRegisterResponse;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.model.PartyRouteComment;
import org.prgrms.wumo.domain.comment.repository.PartyRouteCommentRepository;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.route.model.Route;
import org.prgrms.wumo.domain.route.repository.RouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	private void validateLocation(Long locationId){
		if (!locationRepository.existsById(locationId))
			throw new EntityNotFoundException("댓글을 작성하고자 하는 후보지가 존재하지 않습니다");
	}
}
