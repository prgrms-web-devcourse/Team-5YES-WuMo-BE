package org.prgrms.wumo.domain.party.repository;

import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long>, PartyMemberCustomRepository {

	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM PartyMember pm WHERE pm.party = :party")
	void deleteAllByParty(Party party);

}
