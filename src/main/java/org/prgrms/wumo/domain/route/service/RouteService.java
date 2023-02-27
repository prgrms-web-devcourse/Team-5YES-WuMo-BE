package org.prgrms.wumo.domain.route.service;

import static org.prgrms.wumo.global.jwt.JwtUtil.getMemberId;
import static org.prgrms.wumo.global.mapper.RouteMapper.toRoute;
import static org.prgrms.wumo.global.mapper.RouteMapper.toRouteRegisterResponse;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.party.repository.PartyRepository;
import org.prgrms.wumo.domain.route.dto.request.RouteRegisterRequest;
import org.prgrms.wumo.domain.route.dto.response.RouteRegisterResponse;
import org.prgrms.wumo.domain.route.model.Route;
import org.prgrms.wumo.domain.route.repository.RouteRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteService {

	private final RouteRepository routeRepository;
	private final PartyRepository partyRepository;
	private final PartyMemberRepository partyMemberRepository;
	private final LocationRepository locationRepository;

	public RouteRegisterResponse registerRoute(RouteRegisterRequest routeRegisterRequest) {
		Party party = partyRepository.findById(routeRegisterRequest.partyId())
			.orElseThrow(() -> new EntityNotFoundException("일치하는 모임이 없습니다."));
		Location location = locationRepository.findById(routeRegisterRequest.locationId())
			.orElseThrow(() -> new EntityNotFoundException("일치하는 후보지가 없습니다."));

		validateAccess(party);

		if (routeRegisterRequest.routeId() == null) {
			Route route = routeRepository.save(toRoute(location, party));
			return toRouteRegisterResponse(route.getId());
		}

		Route route = routeRepository.findById(routeRegisterRequest.routeId())
			.orElseThrow(() -> new EntityNotFoundException("일치하는 루트가 없습니다."));
		route.updateLocation(location);

		return toRouteRegisterResponse(route.getId());
	}

	private void validateAccess(Party party) {
		if (isNotPartyMember(party.getId())) {
			throw new AccessDeniedException("잘못된 접근입니다.");
		}
	}

	private boolean isNotPartyMember(long partyId) {
		return !partyMemberRepository.existsByPartyIdAndMemberId(partyId, getMemberId());
	}
}
