package org.prgrms.wumo.domain.like.repository;

import java.util.Map;

public interface RouteLikeCustomRepository {

	Map<Long, Long> countAllByRouteId(Long cursorId, int pageSize);

	void updateLikeCount(Map<Long, Long> likeCounts);

}
