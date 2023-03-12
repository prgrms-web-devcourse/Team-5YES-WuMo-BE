package org.prgrms.wumo.domain.comment.repository;

import java.util.List;

import org.prgrms.wumo.domain.comment.model.QReplyComment;
import org.prgrms.wumo.domain.comment.model.ReplyComment;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReplyCommentCustomRepositoryImpl implements ReplyCommentCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QReplyComment qReplyComment = QReplyComment.replyComment;

	@Override
	public List<ReplyComment> findAllByCommentId(Long cursorId, int pageSize, Long commentId) {
		return jpaQueryFactory
				.selectFrom(qReplyComment)
				.where(
						eqCommentId(commentId),
						ltReplyCommentId(cursorId)
				)
				.orderBy(qReplyComment.id.desc())
				.limit(pageSize)
				.fetch();
	}

	private BooleanExpression eqCommentId(Long commentId){
		return qReplyComment.commentId.eq(commentId);
	}

	private BooleanExpression ltReplyCommentId(Long cursorId) {
		return (cursorId != null) ? qReplyComment.id.lt(cursorId) : null;
	}
}
