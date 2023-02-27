package org.prgrms.wumo.global.mapper;

import java.util.List;
import org.prgrms.wumo.domain.location.dto.request.LocationRegisterRequest;
import org.prgrms.wumo.domain.location.dto.response.LocationGetAllResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationGetResponse;
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
				.partyId(locationRegisterRequest.partyId())
				.build();
	}

	public static LocationGetResponse toLocationGetResponse(Location location){
		return new LocationGetResponse(
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
				location.getCategory(),
				// TODO Route 의 코드가 추가되면 route.getId()로 대체
				1L
		);
	}

	public static LocationGetAllResponse toLocationGetAllResponse(List<Location> locations, Long cursorId){
		return new LocationGetAllResponse(
				locations.stream().map(LocationMapper::toLocationGetResponse).toList(), cursorId
		);
	}
}
