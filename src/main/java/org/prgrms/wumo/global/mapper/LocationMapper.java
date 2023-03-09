package org.prgrms.wumo.global.mapper;

import java.util.ArrayList;
import java.util.List;
import org.prgrms.wumo.domain.location.dto.request.LocationRegisterRequest;
import org.prgrms.wumo.domain.location.dto.response.LocationGetAllResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationGetResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationRegisterResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationSpendingUpdateResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationUpdateResponse;
import org.prgrms.wumo.domain.location.model.Location;

public class LocationMapper {

	public static LocationRegisterResponse toLocationRegisterResponse(Location location){
		return new LocationRegisterResponse(
				location.getId()
		);
	}

	public static Location toLocation(LocationRegisterRequest locationRegisterRequest, Long memberId){
		return Location.builder()
				.memberId(memberId)
				.name(locationRegisterRequest.name())
				.searchAddress(locationRegisterRequest.searchAddress())
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

	public static LocationGetResponse toLocationGetResponse(Location location, boolean isEditable){
		return new LocationGetResponse(
				location.getId(),
				location.getName(),
				location.getSearchAddress(),
				location.getAddress(),
				location.getLatitude(),
				location.getLongitude(),
				location.getImage(),
				location.getDescription(),
				location.getVisitDate(),
				location.getExpectedCost(),
				location.getSpending(),
				location.getCategory(),
				location.getRoute() == null ? null : location.getRoute().getId(),
				isEditable
		);
	}

	public static LocationGetAllResponse toLocationGetAllResponse(List<Location> locations, List<Boolean> isEditables, Long lastId){

		List<LocationGetResponse> locationGetResponses = new ArrayList<>();
		for (int i = 0; i < locations.size(); i++) {
			locationGetResponses.add(toLocationGetResponse(locations.get(i), isEditables.get(i)));
		}

		return new LocationGetAllResponse(
				locationGetResponses, lastId
		);
	}

	public static LocationUpdateResponse toLocationUpdateResponse(Location location){
		return new LocationUpdateResponse(
				location.getId(),
				location.getImage(),
				location.getDescription(),
				location.getVisitDate(),
				location.getExpectedCost(),
				location.getCategory()
		);
	}

	public static LocationSpendingUpdateResponse toLocationSpendingUpdateResponse(int spending){
		return new LocationSpendingUpdateResponse(spending);
	}
}
