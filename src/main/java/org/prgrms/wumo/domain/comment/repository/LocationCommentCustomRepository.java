package org.prgrms.wumo.domain.comment.repository;

import java.util.List;
import org.prgrms.wumo.domain.comment.model.LocationComment;

public interface LocationCommentCustomRepository {
	List<LocationComment> findAllByLocationId(Long locationId, Long cursorId, int pageSize);
}
