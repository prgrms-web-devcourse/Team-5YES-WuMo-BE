package org.prgrms.wumo.domain.location.repository;

import java.util.List;
import org.prgrms.wumo.domain.location.model.Location;

public interface LocationCustomRepository {

	List<Location> findByPartyId(Long cursorId, int pageSize, Long partyId);
}
