package org.prgrms.wumo.domain.route.repository;

import org.prgrms.wumo.domain.route.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RouteRepository extends JpaRepository<Route, Long>, RouteCustomRepository {

	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM Route r WHERE r.party.id = :partyId")
	void deleteAllByPartyId(Long partyId);

}
