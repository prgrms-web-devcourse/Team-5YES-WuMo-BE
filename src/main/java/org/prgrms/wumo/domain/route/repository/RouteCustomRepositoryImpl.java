package org.prgrms.wumo.domain.route.repository;

import java.util.List;

import org.prgrms.wumo.domain.route.model.QRoute;
import org.prgrms.wumo.domain.route.model.Route;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RouteCustomRepositoryImpl implements RouteCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	private final QRoute qRoute = QRoute.route;

	@Override
	public List<Route> findAllByCursor(Long cursorId, int pageSize) {

		return jpaQueryFactory.selectFrom(qRoute)
			.where(
				ltRouteId(cursorId),
				isPublic()
			)
			.orderBy(qRoute.id.desc())
			.limit(pageSize)
			.fetch();
	}

	private BooleanExpression ltRouteId(Long cursorId) {
		if (cursorId == null) {
			return null;
		}

		return qRoute.id.lt(cursorId);
	}

	private BooleanExpression isPublic() {
		return qRoute.isPublic.eq(true);
	}
}
