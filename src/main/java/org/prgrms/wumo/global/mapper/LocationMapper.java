package org.prgrms.wumo.global.mapper;

import org.prgrms.wumo.domain.location.dto.request.LocationRegisterRequest;
import org.prgrms.wumo.domain.location.dto.response.LocationRegisterResponse;
import org.prgrms.wumo.domain.location.model.Location;

public class LocationMapper {

	public static LocationRegisterResponse toLocationRegisterResponse(Location location){
		return new LocationRegisterResponse(
				location.getId()
		);
	}

	public static Location toLocation(LocationRegisterRequest locationRegisterRequest){
		return Location.builder()
				.name(locationRegisterRequest.name())
				.address(locationRegisterRequest.address())
				.latitude(locationRegisterRequest.latitude())
				.longitude(locationRegisterRequest.longitude())
				.category(locationRegisterRequest.category())
				.image(locationRegisterRequest.image())
				.description(locationRegisterRequest.description())
				.visitDate(locationRegisterRequest.visitDate())
				.expectedCost(locationRegisterRequest.expectedCost())
				.party_id(locationRegisterRequest.party_id())
				.build();
	}
}
