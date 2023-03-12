package org.prgrms.wumo.domain.like.repository;

import java.util.List;

import org.prgrms.wumo.domain.like.model.QRouteLike;
import org.prgrms.wumo.domain.route.model.QRoute;
import org.prgrms.wumo.domain.route.model.Route;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RouteLikeCustomRepositoryImpl implements RouteLikeCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	private final JdbcTemplate jdbcTemplate;

	private final QRouteLike qRouteLike = QRouteLike.routeLike;

	private final QRoute qRoute = QRoute.route;

	@Override
	public Pair<List<Long>, List<Route>> findAllByMemberId(Long memberId, Long cursorId, int pageSize) {
		List<Tuple> tuples = jpaQueryFactory
				.select(qRouteLike.id, qRoute)
				.from(qRouteLike)
				.where(
						ltRouteLikeId(cursorId),
						eqMemberId(memberId),
						isPublic()
				)
				.orderBy(qRouteLike.id.desc())
				.limit(pageSize)
				.leftJoin(qRoute)
				.on(qRouteLike.routeId.eq(qRoute.id))
				.fetch();

		List<Long> routeLikeIds = tuples.stream()
				.map(tuple -> tuple.get(0, Long.class))
				.toList();

		List<Route> routes = tuples.stream()
				.map(tuple -> tuple.get(1, Route.class))
				.toList();

		return Pair.of(routeLikeIds, routes);
	}

	@Override
	public List<Pair<Long, Long>> countAllByRouteId(Long cursorId, int batchSize) {
		return jpaQueryFactory
				.select(qRoute.id, qRouteLike.id.count().coalesce(0L))
				.from(qRoute)
				.where(gtRouteId(cursorId))
				.leftJoin(qRouteLike)
				.on(qRouteLike.routeId.eq(qRoute.id))
				.groupBy(qRoute.id)
				.orderBy(qRoute.id.asc())
				.limit(batchSize)
				.fetch()
				.stream()
				.map(row -> Pair.of(row.get(0, Long.class), row.get(1, Long.class)))
				.toList();
	}

	@Override
	public void updateLikeCount(List<Pair<Long, Long>> resultSet) {
		if (resultSet.size() > 0) {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE route SET like_count = CASE ");
			resultSet.forEach(
					pair -> sql.append(String.format("WHEN id = %d THEN %d ", pair.getFirst(), pair.getSecond()))
			);
			sql.append(
					String.format(
							"END WHERE id BETWEEN %d AND %d;", resultSet.get(0).getFirst(), resultSet.get(resultSet.size() - 1).getFirst()
					)
			);

			jdbcTemplate.update(sql.toString());
		}
	}

	private BooleanExpression gtRouteId(Long cursorId) {
		return (cursorId != null) ? qRoute.id.gt(cursorId) : null;
	}

	private BooleanExpression eqMemberId(Long memberId) {
		return qRouteLike.memberId.eq(memberId);
	}

	private BooleanExpression ltRouteLikeId(Long cursorId) {
		return (cursorId != null) ? qRouteLike.id.lt(cursorId) : null;
	}

	private BooleanExpression isPublic() {
		return qRoute.isPublic.eq(true);
	}

}
