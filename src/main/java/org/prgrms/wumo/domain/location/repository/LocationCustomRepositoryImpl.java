package org.prgrms.wumo.domain.location.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.model.QLocation;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LocationCustomRepositoryImpl implements LocationCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	private final QLocation qlocation = QLocation.location;

	@Override
	public List<Location> findByPartyId(Long cursorId, int pageSize, Long partyId) {
		return jpaQueryFactory
				.selectFrom(qlocation)
				.where(
					eqPartyId(partyId),
					gtLocationId(cursorId)
				)
				.orderBy(qlocation.id.asc())
				.limit(pageSize)
				.fetch();
	}

	private BooleanExpression eqPartyId(Long partyId) {
		return qlocation.partyId.eq(partyId);
	}

	private BooleanExpression gtLocationId(Long cursorId) {
		return (cursorId != null) ? qlocation.id.gt(cursorId) : null;
	}
}
