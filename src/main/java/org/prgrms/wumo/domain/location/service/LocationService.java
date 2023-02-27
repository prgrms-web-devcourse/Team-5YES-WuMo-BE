package org.prgrms.wumo.domain.location.service;

import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.prgrms.wumo.domain.location.dto.request.LocationRegisterRequest;
import org.prgrms.wumo.domain.location.dto.response.LocationGetResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationRegisterResponse;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.prgrms.wumo.global.mapper.LocationMapper.toLocation;
import static org.prgrms.wumo.global.mapper.LocationMapper.toLocationGetResponse;
import static org.prgrms.wumo.global.mapper.LocationMapper.toLocationRegisterResponse;

@Service
@RequiredArgsConstructor
public class LocationService {
	private final LocationRepository locationRepository;

	@Transactional
	public LocationRegisterResponse registerLocation(LocationRegisterRequest locationRegisterRequest){
		return toLocationRegisterResponse(locationRepository.save(toLocation(locationRegisterRequest)));
	}

	@Transactional(readOnly = true)
	public LocationGetResponse getLocation(Long locationId){
		return toLocationGetResponse(getLocationEntity(locationId));
	}

	private Location getLocationEntity(Long locationId){
		return locationRepository.findById(locationId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 후보 장소입니다"));
	}
}
