package org.prgrms.wumo.domain.like.repository;

import org.prgrms.wumo.domain.like.model.RouteLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RouteLikeRepository extends JpaRepository<RouteLike, Long>, RouteLikeCustomRepository {

	boolean existsByRouteIdAndMemberId(Long routeId, Long memberId);

	@Modifying
	@Query("DELETE FROM RouteLike routeLike WHERE routeLike.routeId = :routeId")
	void deleteAllByRouteId(Long routeId);

	@Modifying
	@Query("DELETE FROM RouteLike routeLike WHERE routeLike.routeId = :routeId AND routeLike.memberId = :memberId")
	int deleteByRouteIdAndMemberId(Long routeId, Long memberId);

}
