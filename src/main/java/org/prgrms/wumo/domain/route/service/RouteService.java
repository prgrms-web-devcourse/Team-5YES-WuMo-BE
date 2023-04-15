package org.prgrms.wumo.domain.route.service;

import static org.prgrms.wumo.global.jwt.JwtUtil.getMemberId;
import static org.prgrms.wumo.domain.route.mapper.RouteMapper.toRoute;
import static org.prgrms.wumo.domain.route.mapper.RouteMapper.toRouteGetAllResponses;
import static org.prgrms.wumo.domain.route.mapper.RouteMapper.toRouteGetResponse;
import static org.prgrms.wumo.domain.route.mapper.RouteMapper.toRouteRegisterResponse;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.like.repository.RouteLikeRepository;
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
import org.prgrms.wumo.global.exception.ExceptionMessage;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.util.Pair;
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
	private final RouteLikeRepository routeLikeRepository;

	@Transactional
	public RouteRegisterResponse registerRoute(RouteRegisterRequest routeRegisterRequest) {
		long partyId = routeRegisterRequest.partyId();
		Party party = getPartyEntity(partyId);
		Location location = getLocationEntity(routeRegisterRequest.locationId());
		validateAccess(partyId);

		Route route = routeRepository.findByPartyId(partyId)
				.orElseGet(() -> routeRepository.save(toRoute(location, party)));
		route.updateLocation(location);

		return toRouteRegisterResponse(route.getId());
	}

	@Transactional(readOnly = true)
	public RouteGetResponse getRoute(long partyId, int fromPublic) {
		Route route = getRouteEntityByParty(partyId);
		route.addIsLiking(routeLikeRepository.existsByRouteIdAndMemberId(route.getId(), getMemberId()));

		if (fromPublic == 0) {
			validateAccess(route.getParty().getId());
		}

		return toRouteGetResponse(route);
	}

	@Cacheable(
			cacheNames = "routes",
			key = "#routeGetAllRequest.sortType().name()",
			condition = "{#routeGetAllRequest.cursorId() == null && #routeGetAllRequest.searchWord() == null}")
	@Transactional(readOnly = true)
	public RouteGetAllResponses getAllRoute(RouteGetAllRequest routeGetAllRequest) {
		List<Route> routes = routeRepository.findAllByCursorAndSearchWord(
				getCursorRoute(routeGetAllRequest.cursorId()),
				routeGetAllRequest.pageSize(),
				routeGetAllRequest.sortType(),
				routeGetAllRequest.searchWord()
		);
		addIsLiking(routes);

		return toRouteGetAllResponses(routes, getRouteLastId(routes));
	}

	@Transactional(readOnly = true)
	public RouteGetAllResponses getAllLikedRoute(RouteGetAllRequest routeGetAllRequest) {
		Pair<List<Long>, List<Route>> pair = routeLikeRepository.findAllByMemberId(
				getMemberId(),
				routeGetAllRequest.cursorId(),
				routeGetAllRequest.pageSize()
		);

		List<Long> routeLikeIds = pair.getFirst();
		List<Route> routes = pair.getSecond();

		return toRouteGetAllResponses(routes, getRouteLikeLastId(routeLikeIds));
	}

	@CacheEvict(value = "routes", key = "\"NEWEST\"")
	@Transactional
	public void updateRoutePublicStatus(RouteStatusUpdateRequest routeStatusUpdateRequest) {
		Route route = getRouteEntity(routeStatusUpdateRequest.routeId());
		validateAccess(route.getParty().getId());
		route.updatePublicStatus(
				routeStatusUpdateRequest.name(),
				routeStatusUpdateRequest.isPublic()
		);
	}

	private void validateAccess(long partyId) {
		if (isNotPartyMember(partyId)) {
			throw new AccessDeniedException(ExceptionMessage.WRONG_ACCESS.name());
		}
	}

	private boolean isNotPartyMember(long partyId) {
		return !partyMemberRepository.existsByPartyIdAndMemberId(partyId, getMemberId());
	}

	private void addIsLiking(List<Route> routes) {
		long memberId = getMemberId();
		routes.forEach(
				route -> route.addIsLiking(
						routeLikeRepository.existsByRouteIdAndMemberId(route.getId(), memberId)
				));
	}

	private long getRouteLastId(List<Route> routes) {
		long lastId = -1L;
		if (routes.size() != 0) {
			lastId = routes.get(routes.size() - 1).getId();
		}
		return lastId;
	}

	private long getRouteLikeLastId(List<Long> routeLikeIds) {
		long lastId = -1L;
		if (routeLikeIds.size() != 0) {
			lastId = routeLikeIds.get(routeLikeIds.size() - 1);
		}
		return lastId;
	}

	private Route getCursorRoute(Long cursorId) {
		Route cursorRoute = null;
		if(cursorId != null) {
			cursorRoute = getRouteEntity(cursorId);
		}
		return cursorRoute;
	}

	private Route getRouteEntity(long routeId) {
		return routeRepository.findById(routeId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format(ExceptionMessage.ENTITY_NOT_FOUND.name(), ExceptionMessage.ROUTE.name())
				));
	}

	private Party getPartyEntity(long partyId) {
		return partyRepository.findById(partyId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format(ExceptionMessage.ENTITY_NOT_FOUND.name(), ExceptionMessage.PARTY.name())
				));
	}

	private Location getLocationEntity(long locationId) {
		return locationRepository.findById(locationId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format(ExceptionMessage.ENTITY_NOT_FOUND.name(), ExceptionMessage.LOCATION.name())
				));
	}

	private Route getRouteEntityByParty(long partyId) {
		return routeRepository.findByPartyId(partyId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format(ExceptionMessage.ENTITY_NOT_FOUND.name(), ExceptionMessage.ROUTE.name())
				));
	}
}
