package org.prgrms.wumo.domain.comment.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.prgrms.wumo.domain.comment.model.PartyRouteComment;
import org.prgrms.wumo.domain.comment.model.QPartyRouteComment;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PartyRouteCommentCustomRepositoryImpl implements PartyRouteCommentCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	private final QPartyRouteComment qPartyRouteComment = QPartyRouteComment.partyRouteComment;

	@Override
	public List<PartyRouteComment> findAllByLocationId(Long cursorId, int pageSize, Long locationId) {
		return jpaQueryFactory
				.selectFrom(qPartyRouteComment)
				.where(
						eqLocationId(locationId),
						ltPartyRouteCommentId(cursorId)
				)
				.orderBy(qPartyRouteComment.id.desc())
				.limit(pageSize)
				.fetch();
	}

	private BooleanExpression eqLocationId(Long locationId){
		return qPartyRouteComment.locationId.eq(locationId);
	}

	private BooleanExpression gtPartyRouteCommentId(Long cursorId){
		return (cursorId != null) ? qPartyRouteComment.id.gt(cursorId) : null;
	}

	private BooleanExpression ltPartyRouteCommentId(Long cursorId){
		return (cursorId != null) ? qPartyRouteComment.id.lt(cursorId) : null;
	}
}
