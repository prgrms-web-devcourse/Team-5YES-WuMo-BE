package org.prgrms.wumo.global.mapper;

import java.util.List;

import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.party.model.Party;
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
		return Route.builder()
			.locations(List.of(location))
			.party(party)
			.build();
	}
}
