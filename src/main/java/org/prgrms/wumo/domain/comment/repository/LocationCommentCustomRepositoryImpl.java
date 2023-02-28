package org.prgrms.wumo.domain.comment.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.prgrms.wumo.domain.comment.model.LocationComment;
import org.prgrms.wumo.domain.comment.model.QLocationComment;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LocationCommentCustomRepositoryImpl implements LocationCommentCustomRepository{

	private final JPAQueryFactory jpaQueryFactory;

	private final QLocationComment qLocationComment = QLocationComment.locationComment;

	@Override
	public List<LocationComment> findAllByLocationId(Long locationId, Long cursorId, int pageSize) {
		return jpaQueryFactory
				.selectFrom(qLocationComment)
				.where(
						eqLocationId(locationId),
						gtLocationCommentId(cursorId)
				)
				.orderBy(qLocationComment.id.asc())
				.limit(pageSize)
				.fetch();
	}

	private BooleanExpression eqLocationId(Long locationId){
		return qLocationComment.locationId.eq(locationId);
	}

	private BooleanExpression gtLocationCommentId(Long cursorId){
		return (cursorId != null) ? qLocationComment.id.gt(cursorId) : null;
	}
}
