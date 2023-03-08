package org.prgrms.wumo.domain.party.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.prgrms.wumo.domain.member.model.QMember;
import org.prgrms.wumo.domain.party.dto.request.PartyType;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.model.QParty;
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

	private final QMember qMember = QMember.member;

	private final QParty qParty = QParty.party;

	@Override
	public Optional<PartyMember> findByPartyIdAndIsLeader(Long partyId) {
		return Optional.ofNullable(
				jpaQueryFactory
						.selectFrom(qPartyMember)
						.where(
								eqPartyId(partyId),
								isLeader()
						)
						.leftJoin(qPartyMember.party, qParty)
						.fetchJoin()
						.leftJoin(qPartyMember.member, qMember)
						.fetchJoin()
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
						.leftJoin(qPartyMember.party, qParty)
						.fetchJoin()
						.leftJoin(qPartyMember.member, qMember)
						.fetchJoin()
						.fetchOne()
		);
	}

	@Override
	public List<PartyMember> findAllByPartyId(Long partyId, Long cursorId, int pageSize) {
		return jpaQueryFactory
				.selectFrom(qPartyMember)
				.where(
						gtPartyMemberId(cursorId),
						eqPartyId(partyId)
				)
				.orderBy(qPartyMember.id.asc())
				.limit(pageSize)
				.leftJoin(qPartyMember.party, qParty)
				.fetchJoin()
				.leftJoin(qPartyMember.member, qMember)
				.fetchJoin()
				.fetch();
	}

	@Override
	public List<PartyMember> findAllByMemberId(Long memberId, Long cursorId, int pageSize, PartyType partyType) {
		return jpaQueryFactory
				.selectFrom(qPartyMember)
				.where(
						eqMemberId(memberId),
						ltPartyMemberId(cursorId),
						eqPartyType(partyType)
				)
				.orderBy(qPartyMember.party.id.desc())
				.limit(pageSize)
				.leftJoin(qPartyMember.party, qParty)
				.fetchJoin()
				.leftJoin(qPartyMember.member, qMember)
				.fetchJoin()
				.fetch();
	}

	@Override
	public List<Long> countAllByPartyIdIn(List<Long> partyIds) {
		return jpaQueryFactory
				.select(qPartyMember.party.count())
				.from(qPartyMember)
				.where(qPartyMember.party.id.in(partyIds))
				.groupBy(qPartyMember.party.id)
				.orderBy(qPartyMember.party.id.desc())
				.fetch();
	}

	@Override
	public List<PartyMember> findAllByPartyIdInAndIsLeader(List<Long> partyIds) {
		return jpaQueryFactory
				.selectFrom(qPartyMember)
				.where(
						qPartyMember.party.id.in(partyIds),
						isLeader()
				)
				.orderBy(qPartyMember.party.id.desc())
				.leftJoin(qPartyMember.party, qParty)
				.fetchJoin()
				.leftJoin(qPartyMember.member, qMember)
				.fetchJoin()
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

	private BooleanExpression ltPartyMemberId(Long cursorId) {
		return (cursorId != null) ? qPartyMember.id.lt(cursorId) : null;
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

	private BooleanExpression eqPartyType(PartyType partyType) {
		LocalDateTime NOW = LocalDateTime.of(LocalDate.now(ZoneId.of("Asia/Tokyo")), LocalTime.MIN);
		return switch (partyType) {
			case ONGOING -> qPartyMember.party.endDate.goe(NOW);
			case COMPLETED -> qPartyMember.party.endDate.before(NOW);
			case ALL -> null;
		};
	}

}
