package org.prgrms.wumo.domain.comment.repository;

import org.prgrms.wumo.domain.comment.model.ReplyComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long>, ReplyCommentCustomRepository {
}
