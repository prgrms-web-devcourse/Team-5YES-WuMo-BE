package org.prgrms.wumo.domain.route.mapper;

import java.util.ArrayList;
import java.util.List;

import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.route.dto.response.RouteGetAllResponse;
import org.prgrms.wumo.domain.route.dto.response.RouteGetAllResponses;
import org.prgrms.wumo.domain.route.dto.response.RouteGetResponse;
import org.prgrms.wumo.domain.route.dto.response.RouteLocationResponse;
import org.prgrms.wumo.domain.route.dto.response.RouteLocationSimpleResponse;
import org.prgrms.wumo.domain.route.dto.response.RouteRegisterResponse;
import org.prgrms.wumo.domain.route.model.Route;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RouteMapper {

	public static RouteRegisterResponse toRouteRegisterResponse(long routeId) {
		return new RouteRegisterResponse(routeId);
	}

	public static Route toRoute(Location location, Party party) {
		ArrayList<Location> list = new ArrayList<>();
		list.add(location);
		return Route.builder()
				.locations(list)
				.party(party)
				.build();
	}

	public static RouteGetResponse toRouteGetResponse(Route route) {
		List<RouteLocationResponse> routeLocations = route.getLocations().stream()
				.map(location -> new RouteLocationResponse(
						location.getId(),
						location.getName(),
						location.getAddress(),
						location.getLatitude(),
						location.getLongitude(),
						location.getImage(),
						location.getDescription(),
						location.getVisitDate(),
						location.getExpectedCost(),
						location.getSpending(),
						location.getCategory().name())
				)
				.toList();
		return new RouteGetResponse(
				route.getId(),
				route.getName(),
				route.isPublic(),
				routeLocations,
				route.getParty().getId(),
				route.isLiking());
	}

	public static RouteGetAllResponses toRouteGetAllResponses(List<Route> routes, long lastId) {
		List<RouteGetAllResponse> routesResponses = routes.stream()
				.map(route -> new RouteGetAllResponse(
						route.getId(),
						route.getLikeCount(),
						route.isLiking(),
						toRouteLocationSimpleResponse(route.getLocations()),
						route.getName(),
						route.getParty().getStartDate(),
						route.getParty().getEndDate(),
						route.getParty().getCoverImage(),
						route.getParty().getId())
				)
				.toList();
		return new RouteGetAllResponses(routesResponses, lastId);
	}

	private static List<RouteLocationSimpleResponse> toRouteLocationSimpleResponse(List<Location> locations) {
		return locations.stream()
				.map(location -> new RouteLocationSimpleResponse(
						location.getId(),
						location.getName(),
						location.getAddress(),
						location.getImage())
				)
				.toList();
	}
}
