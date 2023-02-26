package org.prgrms.wumo.domain.party.repository;

import java.util.List;

import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {

	List<PartyMember> findAllByMember(Member member);

}
