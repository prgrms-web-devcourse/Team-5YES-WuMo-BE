package org.prgrms.wumo.domain.member.repository;

import java.util.Optional;

import org.prgrms.wumo.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
	boolean existsByNickname(String nickname);

	@Query(value = "select m from Member m where m.email.email = :email")
	Optional<Member> findByEmail(String email);
}
