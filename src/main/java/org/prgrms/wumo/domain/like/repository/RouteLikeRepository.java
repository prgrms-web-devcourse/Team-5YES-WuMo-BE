package org.prgrms.wumo.domain.like.repository;

import org.prgrms.wumo.domain.like.model.RouteLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteLikeRepository extends JpaRepository<RouteLike, Long> {

	boolean existsByRouteIdAndMemberId(Long routeId, Long memberId);

}
