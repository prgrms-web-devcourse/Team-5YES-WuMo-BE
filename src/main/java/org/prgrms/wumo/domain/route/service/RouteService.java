package org.prgrms.wumo.domain.route.service;

import static org.prgrms.wumo.global.jwt.JwtUtil.getMemberId;
import static org.prgrms.wumo.global.mapper.RouteMapper.toRoute;
import static org.prgrms.wumo.global.mapper.RouteMapper.toRouteGetAllResponses;
import static org.prgrms.wumo.global.mapper.RouteMapper.toRouteGetResponse;
import static org.prgrms.wumo.global.mapper.RouteMapper.toRouteRegisterResponse;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.party.repository.PartyRepository;
import org.prgrms.wumo.domain.route.dto.request.RouteGetAllRequest;
import org.prgrms.wumo.domain.route.dto.request.RouteRegisterRequest;
import org.prgrms.wumo.domain.route.dto.request.RouteStatusUpdateRequest;
import org.prgrms.wumo.domain.route.dto.response.RouteGetAllResponses;
import org.prgrms.wumo.domain.route.dto.response.RouteGetResponse;
import org.prgrms.wumo.domain.route.dto.response.RouteRegisterResponse;
import org.prgrms.wumo.domain.route.model.Route;
import org.prgrms.wumo.domain.route.repository.RouteRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteService {

	private final RouteRepository routeRepository;
	private final PartyRepository partyRepository;
	private final PartyMemberRepository partyMemberRepository;
	private final LocationRepository locationRepository;

	@Transactional
	public RouteRegisterResponse registerRoute(RouteRegisterRequest routeRegisterRequest) {
		Party party = partyRepository.findById(routeRegisterRequest.partyId())
			.orElseThrow(() -> new EntityNotFoundException("일치하는 모임이 없습니다."));
		Location location = locationRepository.findById(routeRegisterRequest.locationId())
			.orElseThrow(() -> new EntityNotFoundException("일치하는 후보지가 없습니다."));

		validateAccess(party.getId());

		if (routeRegisterRequest.routeId() == null) {
			Route route = routeRepository.save(toRoute(location, party));
			route.updateLocation(location);
			return toRouteRegisterResponse(route.getId());
		}

		Route route = getRouteEntity(routeRegisterRequest.routeId());
		route.updateLocation(location);

		return toRouteRegisterResponse(route.getId());
	}

	@Transactional(readOnly = true)
	public RouteGetResponse getRoute(long routeId, int fromPublic) {
		Route route = getRouteEntity(routeId);

		if (fromPublic == 0) {
			validateAccess(route.getParty().getId());
		}

		return toRouteGetResponse(route);
	}

	@Transactional(readOnly = true)
	public RouteGetAllResponses getAllRoute(RouteGetAllRequest routeGetAllRequest) {
		List<Route> routes = routeRepository.findAllByCursorAndSearchWord(
			routeGetAllRequest.cursorId(),
			routeGetAllRequest.pageSize(),
			routeGetAllRequest.sortType(),
			routeGetAllRequest.searchWord());

		long lastId = -1L;
		if (routes.size() != 0) {
			lastId = routes.get(routes.size() - 1).getId();
		}

		return toRouteGetAllResponses(routes, lastId);
	}

	@Transactional
	public void updateRoutePublicStatus(RouteStatusUpdateRequest routeStatusUpdateRequest) {
		Route route = getRouteEntity(routeStatusUpdateRequest.routeId());
		validateAccess(route.getParty().getId());
		route.updatePublicStatus(routeStatusUpdateRequest.isPublic());
	}

	private Route getRouteEntity(long routeId) {
		return routeRepository.findById(routeId)
			.orElseThrow(() -> new EntityNotFoundException("일치하는 루트가 없습니다."));
	}

	private void validateAccess(long partyId) {
		if (isNotPartyMember(partyId)) {
			throw new AccessDeniedException("잘못된 접근입니다.");
		}
	}

	private boolean isNotPartyMember(long partyId) {
		return !partyMemberRepository.existsByPartyIdAndMemberId(partyId, getMemberId());
	}
}
