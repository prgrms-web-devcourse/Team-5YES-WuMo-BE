package org.prgrms.wumo.domain.location.service;

import static org.prgrms.wumo.global.mapper.LocationMapper.toLocation;
import static org.prgrms.wumo.global.mapper.LocationMapper.toLocationGetResponse;
import static org.prgrms.wumo.global.mapper.LocationMapper.toLocationRegisterResponse;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.prgrms.wumo.domain.location.dto.request.LocationGetAllRequest;
import org.prgrms.wumo.domain.location.dto.request.LocationRegisterRequest;
import org.prgrms.wumo.domain.location.dto.response.LocationGetAllResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationGetResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationRegisterResponse;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.prgrms.wumo.global.mapper.LocationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationService {
	private final LocationRepository locationRepository;

	@Transactional
	public LocationRegisterResponse registerLocation(LocationRegisterRequest locationRegisterRequest) {
		return toLocationRegisterResponse(locationRepository.save(toLocation(locationRegisterRequest)));
	}

	@Transactional(readOnly = true)
	public LocationGetResponse getLocation(Long locationId) {
		return toLocationGetResponse(getLocationEntity(locationId));
	}

	// TODO queryDSL 이용, 커서 기반 페이지네이션으로 변경
	@Transactional(readOnly = true)
	public LocationGetAllResponse getAllLocations(LocationGetAllRequest locationGetAllRequest) {
		return new LocationGetAllResponse(
				locationRepository
						.findAllByPartyId(locationGetAllRequest.partyId())
						.stream()
						.map(LocationMapper::toLocationGetResponse)
						.toList(),
				10L
		);
	}

	private Location getLocationEntity(Long locationId) {
		return locationRepository.findById(locationId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 후보 장소입니다"));
	}
}
