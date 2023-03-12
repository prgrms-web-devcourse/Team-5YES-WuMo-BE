package org.prgrms.wumo.domain.comment.service;

import static org.prgrms.wumo.global.exception.ExceptionMessage.COMMENT;
import static org.prgrms.wumo.global.exception.ExceptionMessage.WRONG_ACCESS;
import static org.prgrms.wumo.global.jwt.JwtUtil.getMemberId;
import static org.prgrms.wumo.global.mapper.CommentMapper.toReplyComment;
import static org.prgrms.wumo.global.mapper.CommentMapper.toReplyCommentGetAllResponse;
import static org.prgrms.wumo.global.mapper.CommentMapper.toReplyCommentRegisterResponse;
import static org.prgrms.wumo.global.exception.ExceptionMessage.ENTITY_NOT_FOUND;


import java.util.List;
import java.util.Objects;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.comment.dto.request.ReplyCommentGetAllRequest;
import org.prgrms.wumo.domain.comment.dto.request.ReplyCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.response.ReplyCommentGetAllResponse;
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
		ReplyComment replyComment = toReplyComment(replyCommentRegisterRequest, locationComment.getMember());
		return toReplyCommentRegisterResponse(replyCommentRepository.save(replyComment));
	}

	@Transactional(readOnly = true)
	public ReplyCommentGetAllResponse getAllReplyComment(ReplyCommentGetAllRequest replyCommentGetAllRequest) {
		List<ReplyComment> replyComments = replyCommentRepository.findAllByCommentId(
				replyCommentGetAllRequest.cursorId(), replyCommentGetAllRequest.pageSize(),
				replyCommentGetAllRequest.commentId()
		);

		List<Boolean> isEditables = replyComments.stream()
				.map(replyComment -> getIsEditable(replyComment, getMemberId()))
				.toList();

		long lastId = (replyComments.size()) > 0 ?
				replyComments.get(replyComments.size() - 1).getId() : -1L;

		return toReplyCommentGetAllResponse(replyComments, isEditables, lastId);
	}

	private LocationComment getLocationCommentEntity(Long locationCommentId) {
		return locationCommentRepository.findById(locationCommentId)
				.orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND.name(), COMMENT.name())));
	}

	private void checkMemberInParty(Long partyMemberId) {
		if (!partyMemberRepository.existsById(partyMemberId))
			throw new AccessDeniedException(String.format(WRONG_ACCESS.name()));
	}

	private boolean getIsEditable(ReplyComment replyComment, Long memberId) {
		return Objects.equals(replyComment.getMember().getId(), memberId);
	}
}
