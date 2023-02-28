package org.prgrms.wumo.domain.comment.repository;

import org.prgrms.wumo.domain.comment.model.LocationComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationCommentRepository extends JpaRepository<LocationComment, Long>, LocationCommentCustomRepository {
}
