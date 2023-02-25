package org.prgrms.wumo.domain.party.repository;

import org.prgrms.wumo.domain.party.model.PartyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {
}
