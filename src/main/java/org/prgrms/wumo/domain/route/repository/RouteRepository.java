package org.prgrms.wumo.domain.route.repository;

import org.prgrms.wumo.domain.route.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long>, RouteCustomRepository {
}
