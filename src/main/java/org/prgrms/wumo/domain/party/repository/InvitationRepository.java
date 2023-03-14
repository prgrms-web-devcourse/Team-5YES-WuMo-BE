package org.prgrms.wumo.domain.party.repository;

import java.util.Optional;

import org.prgrms.wumo.domain.party.model.Invitation;
import org.prgrms.wumo.domain.party.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

	Optional<Invitation> findTopByPartyOrderByIdDesc(Party party);

	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM Invitation i WHERE i.party.id = :partyId")
	void deleteAllByPartyId(Long partyId);

}
