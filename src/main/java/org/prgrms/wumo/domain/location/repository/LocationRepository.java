package org.prgrms.wumo.domain.location.repository;

import java.util.List;
import org.prgrms.wumo.domain.location.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

	@Query(value = "SELECT * FROM location WHERE party_id = :partyId AND id > :cursorId LIMIT :pageSize", nativeQuery = true)
	List<Location> findAllByPartyIdAndCursorIdLimitPageSize(Long partyId, Long cursorId, int pageSize);
}
