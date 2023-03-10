package org.prgrms.wumo.domain.member.repository;

import org.prgrms.wumo.domain.member.model.QMember;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	private final QMember qMember = QMember.member;

	@Override
	public boolean existsByEmail(String email) {
		Integer fetchOne = jpaQueryFactory
				.selectOne()
				.from(qMember)
				.where(qMember.email.email.eq(email))
				.fetchFirst();
		return fetchOne != null;
	}
}
