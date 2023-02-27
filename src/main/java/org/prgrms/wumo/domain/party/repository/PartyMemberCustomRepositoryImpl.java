package org.prgrms.wumo.domain.party.repository;

import java.util.List;

import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.model.QPartyMember;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PartyMemberCustomRepositoryImpl implements PartyMemberCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	private final QPartyMember qPartyMember = QPartyMember.partyMember;

	@Override
	public List<PartyMember> findAllByMemberId(Long memberId, Long cursorId, int pageSize) {
		return jpaQueryFactory
				.selectFrom(qPartyMember)
				.where(
						eqMemberId(memberId),
						gtPartyMemberId(cursorId)
				)
				.orderBy(qPartyMember.id.asc())
				.limit(pageSize)
				.fetch();
	}

	@Override
	public boolean existsByPartyIdAndMemberId(Long partyId, Long memberId) {
		Integer exist = jpaQueryFactory
				.selectOne()
				.from(qPartyMember)
				.where(qPartyMember.party.id.eq(partyId), qPartyMember.member.id.eq(memberId))
				.fetchFirst();

		return exist != null;
	}

	private BooleanExpression gtPartyMemberId(Long cursorId) {
		return (cursorId != null) ? qPartyMember.id.gt(cursorId) : null;
	}

	private BooleanExpression eqMemberId(Long memberId) {
		return qPartyMember.member.id.eq(memberId);
	}

}
