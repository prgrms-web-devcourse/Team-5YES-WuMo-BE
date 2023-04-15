package org.prgrms.wumo.domain.route.repository;

import java.util.List;
import java.util.Optional;

import org.prgrms.wumo.domain.location.model.QLocation;
import org.prgrms.wumo.domain.route.dto.request.SortType;
import org.prgrms.wumo.domain.route.model.QRoute;
import org.prgrms.wumo.domain.route.model.Route;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
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
	public List<Route> findAllByCursorAndSearchWord(Route route, int pageSize, SortType sortType, String searchWord) {

		return jpaQueryFactory
				.selectFrom(qRoute)
				.where(inRouteAndHasSearchWord(searchWord),
						cursor(route, sortType),
						cursorFilter(route, sortType),
						isPublic())
				.orderBy(getSortType(sortType))
				.limit(pageSize)
				.fetch();
	}

	@Override
	public Optional<Route> findByPartyId(long partyId) {
		Route route = jpaQueryFactory
				.selectFrom(qRoute)
				.where(qRoute.party.id.eq(partyId))
				.fetchFirst();
		return Optional.ofNullable(route);
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

	private Predicate cursor(Route cursorRoute, SortType sortType) {

		if (cursorRoute == null) {
			return null;
		}

		switch (sortType) {
			case LIKES:
				return qRoute.likeCount.loe(cursorRoute.getLikeCount());
			default:
				return qRoute.id.lt(cursorRoute.getId());
		}
	}

	private Predicate cursorFilter(Route cursorRoute, SortType sortType) {

		if (cursorRoute == null) {
			return null;
		}

		if (sortType == SortType.LIKES) {
			return ExpressionUtils.or(qRoute.likeCount.ne(cursorRoute.getLikeCount()), qRoute.id.lt(cursorRoute.getId()));
		}
		return null;
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
