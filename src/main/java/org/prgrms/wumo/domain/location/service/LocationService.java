package org.prgrms.wumo.domain.location.service;

import static org.prgrms.wumo.global.jwt.JwtUtil.getMemberId;
import static org.prgrms.wumo.global.mapper.LocationMapper.toLocation;
import static org.prgrms.wumo.global.mapper.LocationMapper.toLocationGetAllResponse;
import static org.prgrms.wumo.global.mapper.LocationMapper.toLocationGetResponse;
import static org.prgrms.wumo.global.mapper.LocationMapper.toLocationRegisterResponse;
import static org.prgrms.wumo.global.mapper.LocationMapper.toLocationSpendingUpdateResponse;
import static org.prgrms.wumo.global.mapper.LocationMapper.toLocationUpdateResponse;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.location.dto.request.LocationGetAllRequest;
import org.prgrms.wumo.domain.location.dto.request.LocationRegisterRequest;
import org.prgrms.wumo.domain.location.dto.request.LocationSpendingUpdateRequest;
import org.prgrms.wumo.domain.location.dto.request.LocationUpdateRequest;
import org.prgrms.wumo.domain.location.dto.response.LocationGetAllResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationGetResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationRegisterResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationSpendingUpdateResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationUpdateResponse;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService {

	private final LocationRepository locationRepository;
	private final PartyMemberRepository partyMemberRepository;

	@Transactional
	public LocationRegisterResponse registerLocation(LocationRegisterRequest locationRegisterRequest) {
		return toLocationRegisterResponse(locationRepository.save(toLocation(locationRegisterRequest)));
	}

	@Transactional(readOnly = true)
	public LocationGetResponse getLocation(Long locationId) {
		return toLocationGetResponse(getLocationEntity(locationId));
	}

	@Transactional(readOnly = true)
	public LocationGetAllResponse getAllLocation(LocationGetAllRequest locationGetAllRequest) {
		List<Location> locations = locationRepository.findByPartyId(locationGetAllRequest.cursorId(),
				locationGetAllRequest.pageSize(), locationGetAllRequest.partyId());

		long lastId = locations.size() > 0 ? locations.get(locations.size() - 1).getId() : -1L;

		return toLocationGetAllResponse(locations, lastId);
	}

	@Transactional
	public LocationUpdateResponse updateLocation(LocationUpdateRequest locationUpdateRequest) {

		checkMemberInParty(locationUpdateRequest.partyId(), getMemberId());

		Location location = getLocationEntity(locationUpdateRequest.id());
		location.update(locationUpdateRequest);

		locationRepository.save(location);

		return toLocationUpdateResponse(location);
	}

	@Transactional
	public void deleteRouteLocation(long locationId) {
		Location location = getLocationEntity(locationId);

		checkMemberInParty(location.getPartyId(), getMemberId());

		location.deleteRoute();
	}

	@Transactional
	public void deleteLocation(Long locationId){
		Location location = getLocationEntity(locationId);

		checkMemberInParty(location.getPartyId(), getMemberId());

		locationRepository.deleteById(locationId);
	}

	@Transactional
	public LocationSpendingUpdateResponse updateSpending(LocationSpendingUpdateRequest locationSpendingUpdateRequest){
		Location location = getLocationEntity(locationSpendingUpdateRequest.locationId());

		checkMemberInParty(location.getPartyId(), getMemberId());

		location.updateSpending(locationSpendingUpdateRequest.spending());

		locationRepository.save(location);

		return toLocationSpendingUpdateResponse(location.getSpending());
	}

	private Location getLocationEntity(Long locationId) {
		return locationRepository.findById(locationId)
				.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 후보 장소입니다"));
	}

	private void checkMemberInParty(Long partyId, Long memberId) {
		if (!partyMemberRepository.existsByPartyIdAndMemberId(partyId, memberId))
			throw new AccessDeniedException("모임에 속하지 않은 회원입니다.");
	}
}
