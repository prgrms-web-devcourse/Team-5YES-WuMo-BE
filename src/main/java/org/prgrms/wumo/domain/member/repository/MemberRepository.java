package org.prgrms.wumo.domain.member.repository;

import org.prgrms.wumo.domain.member.model.Email;
import org.prgrms.wumo.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
	boolean existsByEmail(Email email);
}
