package org.prgrms.wumo.domain.location.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.wumo.domain.location.dto.request.LocationRegisterRequest;
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
	float longitude = 127.028f;
	float latitude = 37.497f;
	LocalDateTime dayToVisit = LocalDateTime.now().plusDays(10);

	Location location = Location.builder()
				.id(1L).image("http://programmers_gangnam_image.com")
				.description("이번에 새로 오픈한 프로그래머스 강남 교육장!! 모니터도 있고 좋은데 화장실이 좀....")
				.latitude(latitude).longitude(longitude)
				.address("강남역 2번출구").visitDate(dayToVisit)
				.category(Category.STUDY).name("프로그래머스 강남 교육장")
				.spending(3000).expectedCost(4000)
				.build();

	@Nested
	@DisplayName("registerLocation을 사용해서 ")
	class RegisterLocation{
		// Given
		LocationRegisterRequest locationRegisterRequest
				= new LocationRegisterRequest(
						"프로그래머스 강남 교육장", "강남역 2번출구"
				, latitude, longitude, "http://programmers_gangnam_image.com"
				, Category.STUDY, "이번에 새로 오픈한 프로그래머스 강남 교육장!! 모니터도 있고 좋은데 화장실이 좀...."
				, dayToVisit, 4000, 1L
		);

		@Test
		@DisplayName("후보 장소를 등록할 수 있다.")
		void registerLocationTest(){
			// Given
			given(locationRepository.save(any(Location.class))).willReturn(location);

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
			given(locationRepository.findById(1L)).willReturn(Optional.of(location));

			// When
			LocationGetResponse locationGetResponse =
					locationService.getLocation(1L);

			// Then
			assertThat(locationGetResponse.id()).isEqualTo(1L);
			assertThat(locationGetResponse.latitude()).isEqualTo(latitude);
			assertThat(locationGetResponse.longitude()).isEqualTo(longitude);
			assertThat(locationGetResponse.spending()).isEqualTo(3000);
			assertThat(locationGetResponse.expectedCost()).isEqualTo(4000);
			assertThat(locationGetResponse.visitDate()).isEqualTo(dayToVisit);
			assertThat(locationGetResponse.category()).isEqualTo(Category.STUDY);
			assertThat(locationGetResponse.name()).isEqualTo("프로그래머스 강남 교육장");
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
}
