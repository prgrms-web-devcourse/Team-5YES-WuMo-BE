package org.prgrms.wumo.domain.like.repository;

import java.util.List;

import org.prgrms.wumo.domain.route.model.Route;
import org.springframework.data.util.Pair;

public interface RouteLikeCustomRepository {

	Pair<List<Long>, List<Route>> findAllByMemberId(Long memberId, Long cursorId, int pageSize);

	List<Pair<Long, Long>> countAllByRouteId(Long cursorId, int batchSize);

	void updateLikeCount(List<Pair<Long, Long>> resultSet);

}
