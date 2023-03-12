package org.prgrms.wumo.domain.comment.repository;

import java.util.List;

import org.prgrms.wumo.domain.comment.model.ReplyComment;

public interface ReplyCommentCustomRepository {
	List<ReplyComment> findAllByCommentId(Long cursorId, int pageSize, Long commentId);
}
