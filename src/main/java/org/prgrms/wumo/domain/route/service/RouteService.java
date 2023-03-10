package org.prgrms.wumo.domain.route.service;

import static org.prgrms.wumo.global.jwt.JwtUtil.getMemberId;
import static org.prgrms.wumo.global.mapper.RouteMapper.toRoute;
import static org.prgrms.wumo.global.mapper.RouteMapper.toRouteGetAllResponses;
import static org.prgrms.wumo.global.mapper.RouteMapper.toRouteGetResponse;
import static org.prgrms.wumo.global.mapper.RouteMapper.toRouteRegisterResponse;

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
		Party party = partyRepository.findById(routeRegisterRequest.partyId())
				.orElseThrow(() -> new EntityNotFoundException(
						String.format(ExceptionMessage.ENTITY_NOT_FOUND.name(), ExceptionMessage.PARTY.name())
				));
		Location location = locationRepository.findById(routeRegisterRequest.locationId())
				.orElseThrow(() -> new EntityNotFoundException(
						String.format(ExceptionMessage.ENTITY_NOT_FOUND.name(), ExceptionMessage.LOCATION.name())
				));

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
	public RouteGetResponse getRoute(long partyId, int fromPublic) {
		Route route = routeRepository.findByPartyId(partyId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format(ExceptionMessage.ENTITY_NOT_FOUND.name(), ExceptionMessage.ROUTE.name())
				));

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
		addIsLiking(routes);

		//TODO 현재 모든 목록 조회에서 같은 로직 사용중 -> util로 빼는것 고려하기
		long lastId = -1L;
		if (routes.size() != 0) {
			lastId = routes.get(routes.size() - 1).getId();
		}

		return toRouteGetAllResponses(routes, lastId);
	}

	@Transactional(readOnly = true)
	public RouteGetAllResponses getAllLikedRoute(RouteGetAllRequest routeGetAllRequest) {
		Pair<List<Long>, List<Route>> pair = routeLikeRepository.findAllByMemberId(
				getMemberId(),
				routeGetAllRequest.cursorId(),
				routeGetAllRequest.pageSize());

		List<Route> routes = pair.getSecond();
		addIsLiking(routes);

		long lastId = -1L;
		List<Long> routeLikeIds = pair.getFirst();
		if (routeLikeIds.size() != 0) {
			lastId = routeLikeIds.get(routeLikeIds.size() - 1);
		}

		return toRouteGetAllResponses(routes, lastId);
	}

	@Transactional
	public void updateRoutePublicStatus(RouteStatusUpdateRequest routeStatusUpdateRequest) {
		Route route = getRouteEntity(routeStatusUpdateRequest.routeId());
		validateAccess(route.getParty().getId());
		route.updatePublicStatus(routeStatusUpdateRequest.name(), routeStatusUpdateRequest.isPublic());
	}

	private Route getRouteEntity(long routeId) {
		return routeRepository.findById(routeId)
				.orElseThrow(() -> new EntityNotFoundException(
						String.format(ExceptionMessage.ENTITY_NOT_FOUND.name(), ExceptionMessage.ROUTE.name())
				));
	}

	private void addIsLiking(List<Route> routes) {
		long memberId = getMemberId();
		routes.forEach(
				route -> route.addIsLiking(
						routeLikeRepository.existsByRouteIdAndMemberId(route.getId(), memberId)
				));
	}

	private void validateAccess(long partyId) {
		if (isNotPartyMember(partyId)) {
			throw new AccessDeniedException(ExceptionMessage.WRONG_ACCESS.name());
		}
	}

	private boolean isNotPartyMember(long partyId) {
		return !partyMemberRepository.existsByPartyIdAndMemberId(partyId, getMemberId());
	}
}
