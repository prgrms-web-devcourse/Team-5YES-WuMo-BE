package org.prgrms.wumo.domain.comment.repository;

import org.prgrms.wumo.domain.comment.model.PartyRouteComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRouteCommentRepository extends JpaRepository<PartyRouteComment, Long> {
}
