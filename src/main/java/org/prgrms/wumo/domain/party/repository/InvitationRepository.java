package org.prgrms.wumo.domain.party.repository;

import java.util.Optional;

import org.prgrms.wumo.domain.party.model.Invitation;
import org.prgrms.wumo.domain.party.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

	Optional<Invitation> findTopByPartyOrderByIdDesc(Party party);

}
