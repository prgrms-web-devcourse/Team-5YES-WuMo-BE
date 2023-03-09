package org.prgrms.wumo.domain.comment.service;

import static org.prgrms.wumo.global.mapper.CommentMapper.toReplyComment;
import static org.prgrms.wumo.global.mapper.CommentMapper.toReplyCommentRegisterResponse;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.comment.dto.request.ReplyCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.response.ReplyCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.model.LocationComment;
import org.prgrms.wumo.domain.comment.model.ReplyComment;
import org.prgrms.wumo.domain.comment.repository.LocationCommentRepository;
import org.prgrms.wumo.domain.comment.repository.ReplyCommentRepository;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyCommentService {
	private final ReplyCommentRepository replyCommentRepository;
	private final LocationCommentRepository locationCommentRepository;
	private final PartyMemberRepository partyMemberRepository;

	@Transactional
	public ReplyCommentRegisterResponse registerReplyComment(ReplyCommentRegisterRequest replyCommentRegisterRequest) {
		LocationComment locationComment = getLocationCommentEntity(replyCommentRegisterRequest.commentId());
		checkMemberInParty(locationComment.getPartyMember().getId());
		ReplyComment replyComment = toReplyComment(replyCommentRegisterRequest);
		replyComment.setMember(locationComment.getMember());
		return toReplyCommentRegisterResponse(replyCommentRepository.save(replyComment));
	}

	private LocationComment getLocationCommentEntity(Long locationCommentId) {
		return locationCommentRepository.findById(locationCommentId)
				.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 후보지 댓글입니다."));
	}

	private void checkMemberInParty(Long partyMemberId) {
		if (!partyMemberRepository.existsById(partyMemberId))
			throw new AccessDeniedException("모임 내 회원이 아닙니다.");
	}
}
