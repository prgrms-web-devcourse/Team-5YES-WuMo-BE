package org.prgrms.wumo.domain.route.repository;

import java.util.List;

import org.prgrms.wumo.domain.location.model.QLocation;
import org.prgrms.wumo.domain.route.dto.request.SortType;
import org.prgrms.wumo.domain.route.model.QRoute;
import org.prgrms.wumo.domain.route.model.Route;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RouteCustomRepositoryImpl implements RouteCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	private final QRoute qRoute = QRoute.route;
	private final QLocation qLocation = QLocation.location;

	@Override
	public List<Route> findAllByCursorAndSearchWord(Long cursorId, int pageSize, SortType sortType, String searchWord) {

		//TODO join해서 내가 좋아요 눌렀는지 여부까지 가져올 수 있을지
		return jpaQueryFactory
			.selectFrom(qRoute)
			.where(inRouteAndHasSearchWord(searchWord),
				ltRouteId(cursorId),
				isPublic())
			.orderBy(getSortType(sortType))
			.limit(pageSize)
			.fetch();
	}

	private BooleanExpression inRouteAndHasSearchWord(String searchWord) {
		if (searchWord == null) {
			return null;
		}

		return JPAExpressions
			.selectFrom(qLocation)
			.where(
				qLocation.route.eq(qRoute),
				qLocation.address.startsWith(searchWord))
			.exists();
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

	private OrderSpecifier<Long> getSortType(SortType sortType) {
		if (sortType.equals(SortType.NEWEST)) {
			return qRoute.id.desc();
		}
		return qRoute.likeCount.desc();
	}
}
