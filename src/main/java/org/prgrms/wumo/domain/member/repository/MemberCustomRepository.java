package org.prgrms.wumo.domain.member.repository;

public interface MemberCustomRepository {
	boolean existsByEmail(String email);
}
