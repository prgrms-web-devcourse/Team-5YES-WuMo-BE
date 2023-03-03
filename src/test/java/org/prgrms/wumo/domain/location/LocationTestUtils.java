package org.prgrms.wumo.domain.location;

import java.time.LocalDateTime;
import java.util.List;
import org.prgrms.wumo.domain.location.model.Category;
import org.prgrms.wumo.domain.location.model.Location;

public class LocationTestUtils {
	final float longitude1 = 127.028f;
	final float latitude1 = 37.497f;
	LocalDateTime dayToVisit = LocalDateTime.now().plusDays(10);

	Location location1 = Location.builder()
			.id(1L).image("http://programmers_gangnam_image.com")
			.description("이번에 새로 오픈한 프로그래머스 강남 교육장!! 모니터도 있고 좋은데 화장실이 좀....")
			.latitude(latitude1).longitude(longitude1)
			.address("강남역 2번출구").visitDate(dayToVisit)
			.searchAddress("서울특별시 강남구")
			.category(Category.STUDY).name("프로그래머스 강남 교육장")
			.spending(3000).expectedCost(4000)
			.partyId(1L)
			.build();

	Location location2 = Location.builder()
			.id(2L).image("http://grepp_image")
			.description("그렙!!")
			.latitude(latitude1).longitude(longitude1)
			.address("서울특별시 서초구 강남대로327 2층 프로그래머스(서초동, 대륭서초타워)")
			.visitDate(dayToVisit)
			.searchAddress("서울특별시 서초구")
			.category(Category.STUDY).name("프로그래머스 대륭 서초 타워")
			.spending(2000).expectedCost(5000)
			.partyId(1L)
			.build();

	Location location3 = Location.builder()
			.id(3L).image("http://gang_name_station_starbucks")
			.description("하태하태 강남역 스벅 강남 R점")
			.latitude(latitude1).longitude(longitude1)
			.address("서울 강남구 강남대로 30로").visitDate(dayToVisit)
			.searchAddress("서울특별시 강남구")
			.category(Category.COFFEE).name("강남역 R점")
			.spending(6000).expectedCost(5500)
			.partyId(1L)
			.build();

	Location location4 = Location.builder()
			.id(4L).image("http://four_image")
			.description("4번째 핫플")
			.latitude(latitude1).longitude(longitude1)
			.address("서울 강남구 강남대로 30로").visitDate(dayToVisit)
			.searchAddress("서울특별시 강남구")
			.category(Category.COFFEE).name("강남역 R점")
			.spending(6000).expectedCost(5500)
			.partyId(1L)
			.build();

	Location location5 = Location.builder()
			.id(5L).image("http://five_image.com")
			.description("5번 째 핫풀")
			.latitude(latitude1).longitude(longitude1)
			.address("서울 강남구 강남대로 30로").visitDate(dayToVisit)
			.searchAddress("서울특별시 강남구")
			.category(Category.COFFEE).name("강남역 R점")
			.spending(6000).expectedCost(5500)
			.partyId(2L)
			.build();

	Location location6 = Location.builder()
			.id(6L).image("http://six_image.com")
			.description("6번째 핫플")
			.latitude(latitude1).longitude(longitude1)
			.address("서울 강남구 강남대로 30로").visitDate(dayToVisit)
			.searchAddress("서울특별시 강남구")
			.category(Category.COFFEE).name("강남역 R점")
			.spending(6000).expectedCost(5500)
			.partyId(1L)
			.build();
	Location location7 = Location.builder()
			.id(7L).image("http://seven_image")
			.description("7번째 핫플")
			.latitude(latitude1).longitude(longitude1)
			.address("서울 강남구 강남대로 30로").visitDate(dayToVisit)
			.searchAddress("서울특별시 강남구")
			.category(Category.COFFEE).name("강남역 R점")
			.spending(6000).expectedCost(5500)
			.partyId(1L)
			.build();

	public List<Location> getLocations() {
		return List.of(location1, location2, location3, location4, location5, location6, location7);
	}

	public Location getLocation() {
		return location1;
	}

	public Float getLatitude1() {
		return latitude1;
	}

	public Float getLongitude1() {
		return longitude1;
	}

	public LocalDateTime getDayToVisit() {
		return dayToVisit;
	}
}
