package org.prgrms.wumo.domain.comment.service;

import static org.prgrms.wumo.global.jwt.JwtUtil.getMemberId;
import static org.prgrms.wumo.global.mapper.CommentMapper.toLocationComment;
import static org.prgrms.wumo.global.mapper.CommentMapper.toLocationCommentRegisterResponse;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.prgrms.wumo.domain.comment.dto.request.LocationCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.model.LocationComment;
import org.prgrms.wumo.domain.comment.repository.LocationCommentRepository;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationCommentService {
	private final LocationCommentRepository locationCommentRepository;
	private final MemberRepository memberRepository;
	private final PartyMemberRepository partyMemberRepository;

	@Transactional
	public LocationCommentRegisterResponse registerLocationComment(
			LocationCommentRegisterRequest locationCommentRegisterRequest) {

		LocationComment locationComment = toLocationComment(locationCommentRegisterRequest);
		locationComment.setMember(
				memberRepository.findById(getMemberId()).orElseThrow(() -> new EntityNotFoundException("존재 하지 않는 회원입니다"))
		);

		locationComment.setPartyMember(
				partyMemberRepository.findById(locationCommentRegisterRequest.partyMemberId())
						.orElseThrow(() -> new EntityNotFoundException("모임에서 역할이 존재하지 않는 회원입니다"))
		);

		return toLocationCommentRegisterResponse(
				locationCommentRepository.save(locationComment)
		);
	}
}
