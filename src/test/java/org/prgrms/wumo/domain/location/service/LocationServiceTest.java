package org.prgrms.wumo.domain.location.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.prgrms.wumo.global.mapper.LocationMapper.toLocationGetResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.wumo.domain.location.dto.request.LocationGetAllRequest;
import org.prgrms.wumo.domain.location.dto.request.LocationRegisterRequest;
import org.prgrms.wumo.domain.location.dto.response.LocationGetAllResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationGetResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationRegisterResponse;
import org.prgrms.wumo.domain.location.model.Category;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("LocationService 에서 ")
public class LocationServiceTest {

	@InjectMocks
	LocationService locationService;

	@Mock
	LocationRepository locationRepository;

	// GIVEN
	float longitude1 = 127.028f;
	float latitude1 = 37.497f;
	LocalDateTime dayToVisit = LocalDateTime.now().plusDays(10);

	Location location1 = Location.builder()
				.id(1L).image("http://programmers_gangnam_image.com")
				.description("이번에 새로 오픈한 프로그래머스 강남 교육장!! 모니터도 있고 좋은데 화장실이 좀....")
				.latitude(latitude1).longitude(longitude1)
				.address("강남역 2번출구").visitDate(dayToVisit)
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
				.category(Category.STUDY).name("프로그래머스 대륭 서초 타워")
				.spending(2000).expectedCost(5000)
			.partyId(1L)
				.build();

	Location location3 = Location.builder()
				.id(3L).image("http://gang_name_station_starbucks")
				.description("하태하태 강남역 스벅 강남 R점")
				.latitude(latitude1).longitude(longitude1)
				.address("서울 강남구 강남대로 30로").visitDate(dayToVisit)
				.category(Category.COFFEE).name("강남역 R점")
				.spending(6000).expectedCost(5500)
			.partyId(1L)
				.build();

	@Nested
	@DisplayName("registerLocation을 사용해서 ")
	class RegisterLocation{
		// Given
		LocationRegisterRequest locationRegisterRequest
				= new LocationRegisterRequest(
						"프로그래머스 강남 교육장", "강남역 2번출구"
				, latitude1, longitude1, "http://programmers_gangnam_image.com"
				, Category.STUDY, "이번에 새로 오픈한 프로그래머스 강남 교육장!! 모니터도 있고 좋은데 화장실이 좀...."
				, dayToVisit, 4000, 1L
		);

		@Test
		@DisplayName("후보 장소를 등록할 수 있다.")
		void registerLocationTest(){
			// Given
			given(locationRepository.save(any(Location.class))).willReturn(location1);

			// When
			LocationRegisterResponse locationRegisterResponse =
					locationService.registerLocation(locationRegisterRequest);

			// Then
			assertThat(locationRegisterResponse.id()).isEqualTo(1L);

		}
	}

	@Nested
	@DisplayName("getLocation을 사용해서 ")
	class GetLocation{
		// Given

		@Test
		@DisplayName("후보 장소 하나를 상세 조회할 수 있다.")
		void getOneLocationTest(){
			// Given
			given(locationRepository.findById(1L)).willReturn(Optional.of(location1));

			// When
			LocationGetResponse locationGetResponse =
					locationService.getLocation(1L);

			// Then
			assertThat(locationGetResponse).usingRecursiveComparison().isEqualTo(toLocationGetResponse(location1));
		}

		@Test
		@DisplayName(" 없는 후보 장소는 상세 조회할 수 없다.")
		void getLocationFailTest(){
			// Given
			given(locationRepository.findById(1L)).willReturn(Optional.empty());

			// When // Then
			assertThatThrownBy(
					() -> locationService.getLocation(1L)
			).isInstanceOf(EntityNotFoundException.class);
		}
	}

	@Nested
	@DisplayName("getAllLocation을 사용해서 ")
	class getAllLocations{
		// Given
		List<Location> locations = List.of(
				location1, location2, location3
		);

		LocationGetAllRequest locationGetAllRequest
				= new LocationGetAllRequest(0L, 15, 1L);

		@Test
		@DisplayName("해당 모임의 모든 후보 장소를 가져올 수 있다.")
		void getAllLocationsTest(){
			// Given
			given(locationRepository.findAllByPartyId(1L)).willReturn(locations);

			// When
			LocationGetAllResponse locationGetAllResponse = locationService.getAllLocations(locationGetAllRequest);

			// Then
			assertThat(locationGetAllResponse.locations().size()).isEqualTo(3);
			for (int i = 0; i < 3; i++) {
				assertThat(locationGetAllResponse.locations().get(i)).usingRecursiveComparison()
						.isEqualTo(toLocationGetResponse(locations.get(i)));
			}
		}

	}
}
