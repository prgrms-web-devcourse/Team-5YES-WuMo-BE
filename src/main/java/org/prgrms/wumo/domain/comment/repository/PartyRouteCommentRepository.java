package org.prgrms.wumo.domain.comment.repository;

import org.prgrms.wumo.domain.comment.model.PartyRouteComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PartyRouteCommentRepository extends JpaRepository<PartyRouteComment, Long>, PartyRouteCommentCustomRepository {

	void deleteAllByLocationId(Long locationId);

	void deleteAllByRouteId(Long routeId);

	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM PartyRouteComment c WHERE c.partyMember.id = :partyMemberId")
	void deleteAllByPartyMemberId(Long partyMemberId);

}
