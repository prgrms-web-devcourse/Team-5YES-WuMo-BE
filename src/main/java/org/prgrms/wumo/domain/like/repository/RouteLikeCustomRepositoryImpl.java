package org.prgrms.wumo.domain.like.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.prgrms.wumo.domain.like.model.QRouteLike;
import org.prgrms.wumo.domain.route.model.QRoute;
import org.prgrms.wumo.domain.route.model.Route;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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
	public Map<Long, Long> countAllByRouteId(Long cursorId, int batchSize) {
		List<Tuple> tuples = jpaQueryFactory
				.select(qRouteLike.routeId, qRouteLike.count())
				.from(qRouteLike)
				.where(gtRouteId(cursorId))
				.groupBy(qRouteLike.routeId)
				.orderBy(qRouteLike.routeId.asc())
				.limit(batchSize)
				.fetch();

		HashMap<Long, Long> hashMap = new HashMap<>();
		tuples.forEach(tuple -> hashMap.put(tuple.get(0, Long.class), tuple.get(1, Long.class)));
		return hashMap;
	}

	@Override
	public void updateLikeCount(Map<Long, Long> likeCounts) {
		Timestamp NOW = Timestamp.valueOf(LocalDateTime.now());
		List<Long> routeIds = likeCounts.keySet().stream().toList();

		jdbcTemplate.batchUpdate(
				"UPDATE route SET like_count = ?, updated_at = ? WHERE id = ?",
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setLong(1, likeCounts.get(routeIds.get(i)));
						ps.setTimestamp(2, NOW);
						ps.setLong(3, routeIds.get(i));
					}

					@Override
					public int getBatchSize() {
						return routeIds.size();
					}
				}
		);
	}

	private BooleanExpression gtRouteId(Long cursorId) {
		return (cursorId != null) ? qRouteLike.routeId.gt(cursorId) : null;
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
