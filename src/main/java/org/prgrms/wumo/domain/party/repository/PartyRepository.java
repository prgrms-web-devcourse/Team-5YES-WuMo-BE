package org.prgrms.wumo.domain.party.repository;

import org.prgrms.wumo.domain.party.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
