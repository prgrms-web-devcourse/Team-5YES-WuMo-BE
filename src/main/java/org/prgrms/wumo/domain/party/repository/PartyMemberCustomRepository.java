package org.prgrms.wumo.domain.party.repository;

import java.util.List;

import org.prgrms.wumo.domain.party.model.PartyMember;

public interface PartyMemberCustomRepository {

	List<PartyMember> findAllByMemberId(Long memberId, Long cursorId, int pageSize);

	boolean existsByPartyIdAndMemberId(Long partyId, Long memberId);

}
