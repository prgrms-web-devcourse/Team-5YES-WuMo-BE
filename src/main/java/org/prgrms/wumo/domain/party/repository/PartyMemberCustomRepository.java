package org.prgrms.wumo.domain.party.repository;

import java.util.List;
import java.util.Optional;

import org.prgrms.wumo.domain.party.model.PartyMember;

public interface PartyMemberCustomRepository {

	Optional<PartyMember> findByPartyIdAndIsLeader(Long partyId);

	List<PartyMember> findAllByPartyId(Long partyId, Long cursorId, int pageSize);

	List<PartyMember> findAllByMemberId(Long memberId, Long cursorId, int pageSize);

	boolean existsByPartyIdAndMemberId(Long partyId, Long memberId);

}
