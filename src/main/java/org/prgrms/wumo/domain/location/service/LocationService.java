package org.prgrms.wumo.domain.location.service;

import static org.prgrms.wumo.global.mapper.LocationMapper.toLocation;
import static org.prgrms.wumo.global.mapper.LocationMapper.toLocationGetAllResponse;
import static org.prgrms.wumo.global.mapper.LocationMapper.toLocationGetResponse;
import static org.prgrms.wumo.global.mapper.LocationMapper.toLocationRegisterResponse;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.location.dto.request.LocationGetAllRequest;
import org.prgrms.wumo.domain.location.dto.request.LocationRegisterRequest;
import org.prgrms.wumo.domain.location.dto.response.LocationGetAllResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationGetResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationRegisterResponse;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

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
	// @Transactional(readOnly = true)
	// public LocationGetAllResponse getAllLocations(LocationGetAllRequest locationGetAllRequest) {
	// 	List<Location> locations = locationRepository.findAllByPartyIdAndCursorIdLimitPageSize(
	// 		locationGetAllRequest.partyId(),
	// 		locationGetAllRequest.cursorId(), locationGetAllRequest.pageSize());
	// 	return toLocationGetAllResponse(locations, locationGetAllRequest.cursorId() + locationGetAllRequest.pageSize());
	// }
	@Transactional(readOnly = true)
	public LocationGetAllResponse getAllLocation(LocationGetAllRequest locationGetAllRequest){
		List<Location> locations = locationRepository.findByPartyId(locationGetAllRequest.cursorId(),
				locationGetAllRequest.pageSize(), locationGetAllRequest.partyId());

		long lastId = locations.size() > 0 ? locations.get(locations.size() - 1).getId() : -1L;

		return toLocationGetAllResponse(locations, lastId);
	}

	@Transactional
	public void deleteRouteLocation(long locationId) {
		Location location = getLocationEntity(locationId);
		//TODO 요청한 회원이 후보지가 속한 모임멤버인지 확인하기
		location.deleteRoute();
	}

	private Location getLocationEntity(Long locationId) {
		return locationRepository.findById(locationId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 후보 장소입니다"));
	}
}
