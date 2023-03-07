package org.prgrms.wumo.domain.route.repository;

import java.util.List;
import java.util.Optional;

import org.prgrms.wumo.domain.route.dto.request.SortType;
import org.prgrms.wumo.domain.route.model.Route;

public interface RouteCustomRepository {

	List<Route> findAllByCursorAndSearchWord(Long cursorId, int pageSize, SortType sortType, String searchWord);

	Optional<Route> findByPartyId(long partyId);
}
