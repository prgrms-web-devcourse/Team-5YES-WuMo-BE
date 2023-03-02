package org.prgrms.wumo.domain.comment.repository;

import java.util.List;
import org.prgrms.wumo.domain.comment.model.PartyRouteComment;

public interface PartyRouteCommentCustomRepository {
	List<PartyRouteComment> findAllByLocationId(Long cursorId, int pageSize, Long locationId);
}
