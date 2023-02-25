package org.prgrms.wumo.domain.member.repository;

import java.util.Optional;

import org.prgrms.wumo.domain.member.model.Email;
import org.prgrms.wumo.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
	boolean existsByEmail(Email email);

	boolean existsByNickname(String nickname);

	Optional<Member> findByEmail(Email email);
}
