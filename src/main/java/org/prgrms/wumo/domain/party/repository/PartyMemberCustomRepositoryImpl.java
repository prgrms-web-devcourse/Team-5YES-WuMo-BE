package org.prgrms.wumo.domain.party.repository;

import java.util.List;
import java.util.Optional;

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
	public Optional<PartyMember> findByPartyIdAndIsLeader(Long partyId) {
		return Optional.ofNullable(
				jpaQueryFactory
						.selectFrom(qPartyMember)
						.where(
								eqPartyId(partyId),
								isLeader()
						)
						.fetchOne()
		);
	}

	@Override
	public Optional<PartyMember> findByPartyIdAndMemberId(Long partyId, Long memberId) {
		return Optional.ofNullable(
				jpaQueryFactory
						.selectFrom(qPartyMember)
						.where(
								eqPartyId(partyId),
								eqMemberId(memberId)
						)
						.fetchOne()
		);
	}

	@Override
	public List<PartyMember> findAllByPartyId(Long partyId, Long cursorId, int pageSize) {
		return findAllByColumnId("party", partyId, cursorId, pageSize);
	}

	@Override
	public List<PartyMember> findAllByMemberId(Long memberId, Long cursorId, int pageSize) {
		return findAllByColumnId("member", memberId, cursorId, pageSize);
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

	private List<PartyMember> findAllByColumnId(String column, Long columnId, Long cursorId, int pageSize) {
		return jpaQueryFactory
				.selectFrom(qPartyMember)
				.where(
						switch (column) {
							case "party" -> eqPartyId(columnId);
							case "member" -> eqMemberId(columnId);
							default -> null;
						},
						gtPartyMemberId(cursorId)
				)
				.orderBy(qPartyMember.id.asc())
				.limit(pageSize)
				.fetch();
	}

	private BooleanExpression gtPartyMemberId(Long cursorId) {
		return (cursorId != null) ? qPartyMember.id.gt(cursorId) : null;
	}

	private BooleanExpression eqPartyId(Long partyId) {
		return qPartyMember.party.id.eq(partyId);
	}

	private BooleanExpression isLeader() {
		return qPartyMember.isLeader.eq(true);
	}

	private BooleanExpression eqMemberId(Long memberId) {
		return qPartyMember.member.id.eq(memberId);
	}

}
