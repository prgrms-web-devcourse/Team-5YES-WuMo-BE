package org.prgrms.wumo.domain.location.repository;

import org.prgrms.wumo.domain.location.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>, LocationCustomRepository {

	void deleteAllByPartyId(Long partyId);

}
