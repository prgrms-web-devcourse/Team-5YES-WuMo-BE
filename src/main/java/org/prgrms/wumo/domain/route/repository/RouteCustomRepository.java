package org.prgrms.wumo.domain.route.repository;

import java.util.List;

import org.prgrms.wumo.domain.route.model.Route;

public interface RouteCustomRepository {

	List<Route> findAllByCursor(Long cursorId, int pageSize);
}
