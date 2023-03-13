package org.prgrms.wumo.domain.location.service;

import static org.prgrms.wumo.global.exception.ExceptionMessage.ENTITY_NOT_FOUND;
import static org.prgrms.wumo.global.exception.ExceptionMessage.LOCATION;
import static org.prgrms.wumo.global.exception.ExceptionMessage.PARTY_MEMBER;
import static org.prgrms.wumo.global.exception.ExceptionMessage.WRONG_ACCESS;
import static org.prgrms.wumo.global.jwt.JwtUtil.getMemberId;
import static org.prgrms.wumo.domain.location.mapper.LocationMapper.toLocation;
import static org.prgrms.wumo.domain.location.mapper.LocationMapper.toLocationGetAllResponse;
import static org.prgrms.wumo.domain.location.mapper.LocationMapper.toLocationGetResponse;
import static org.prgrms.wumo.domain.location.mapper.LocationMapper.toLocationRegisterResponse;
import static org.prgrms.wumo.domain.location.mapper.LocationMapper.toLocationSpendingUpdateResponse;
import static org.prgrms.wumo.domain.location.mapper.LocationMapper.toLocationUpdateResponse;

import java.util.List;
import java.util.Objects;

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
import org.prgrms.wumo.domain.party.model.PartyMember;
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

		checkMemberInParty(locationRegisterRequest.partyId(), getMemberId());

		return toLocationRegisterResponse(locationRepository.save(toLocation(locationRegisterRequest, getMemberId())));
	}

	@Transactional(readOnly = true)
	public LocationGetResponse getLocation(Long locationId) {
		Location location = getLocationEntity(locationId);
		return toLocationGetResponse(location, getIsEditable(location, getMemberId()));
	}

	@Transactional(readOnly = true)
	public LocationGetAllResponse getAllLocation(LocationGetAllRequest locationGetAllRequest) {
		List<Location> locations = locationRepository.findByPartyId(locationGetAllRequest.cursorId(),
				locationGetAllRequest.pageSize(), locationGetAllRequest.partyId());
		List<Boolean> isEditables = locations.stream().map((location) -> getIsEditable(location, getMemberId())).toList();

		long lastId = locations.size() > 0 ? locations.get(locations.size() - 1).getId() : -1L;

		return toLocationGetAllResponse(locations, isEditables, lastId);
	}

	@Transactional
	public LocationUpdateResponse updateLocation(LocationUpdateRequest locationUpdateRequest) {
		Location location = getLocationEntity(locationUpdateRequest.id());

		checkAuthorization(location, getMemberId());

		location.update(locationUpdateRequest);

		locationRepository.save(location);

		return toLocationUpdateResponse(location);
	}

	@Transactional
	public void deleteRouteLocation(long locationId) {
		Location location = getLocationEntity(locationId);

		checkAuthorization(location, getMemberId());

		location.deleteRoute();
	}

	@Transactional
	public void deleteLocation(Long locationId) {
		Location location = getLocationEntity(locationId);

		checkAuthorization(location, getMemberId());

		locationRepository.deleteById(locationId);
	}

	@Transactional
	public LocationSpendingUpdateResponse updateSpending(LocationSpendingUpdateRequest locationSpendingUpdateRequest) {
		Location location = getLocationEntity(locationSpendingUpdateRequest.locationId());

		checkMemberInParty(location.getPartyId(), getMemberId());

		location.updateSpending(locationSpendingUpdateRequest.spending());

		locationRepository.save(location);

		return toLocationSpendingUpdateResponse(location.getSpending());
	}

	private Location getLocationEntity(Long locationId) {
		return locationRepository.findById(locationId)
				.orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND.name(), LOCATION.name())));
	}

	private void checkMemberInParty(Long partyId, Long memberId) {
		if (!partyMemberRepository.existsByPartyIdAndMemberId(partyId, memberId))
			throw new AccessDeniedException(WRONG_ACCESS.name());
	}

	private PartyMember getPartyMemberEntity(Long partyId, Long memberId) {
		return partyMemberRepository.findByPartyIdAndMemberId(partyId, memberId)
				.orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND.name(), PARTY_MEMBER.name())));
	}

	private void checkAuthorization(Location location, Long memberId) {
		PartyMember partyMember = getPartyMemberEntity(location.getPartyId(), memberId);
		if (partyMember.isLeader())
			return;
		checkMemberInParty(location.getPartyId(), memberId);
		location.checkAuthorization(memberId);
	}

	private boolean getIsEditable(Location location, Long memberId) {
		PartyMember partyMember = getPartyMemberEntity(location.getPartyId(), memberId);
		return Objects.equals(location.getMemberId(), memberId) || partyMember.isLeader();
	}
}
