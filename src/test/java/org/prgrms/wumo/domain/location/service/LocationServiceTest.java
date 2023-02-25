package org.prgrms.wumo.domain.location.service;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.wumo.domain.location.dto.request.LocationRegisterRequest;
import org.prgrms.wumo.domain.location.dto.response.LocationRegisterResponse;
import org.prgrms.wumo.domain.location.model.Category;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("LocationService 에서 ")
public class LocationServiceTest {

	@InjectMocks
	LocationService locationService;

	@Mock
	LocationRepository locationRepository;

	float longitude = 127.0283f;
	float latitude = 37.4975f;
	LocalDateTime dayToVisit = LocalDateTime.now().plusDays(10);

	@Nested
	@DisplayName("후보 장소를 ")
	class RegisterLocation{
		// Given
		Location location = Location.builder()
				.id(1L).image("http://programmers_gangnam_image.com")
				.description("이번에 새로 오픈한 프로그래머스 강남 교육장!! 모니터도 있고 좋은데 화장실이 좀....")
				.latitude(latitude).longitude(longitude)
				.address("강남역 2번출구").visitDate(dayToVisit)
				.category(Category.STUDY).name("프로그래머스 강남 교육장")
				.spending(3000).expectedCost(4000)
				.build();

		LocationRegisterRequest locationRegisterRequest
				= new LocationRegisterRequest(
						"프로그래머스 강남 교육장", "강남역 2번출구"
				, latitude, longitude, "http://programmers_gangnam_image.com"
				, Category.STUDY, "이번에 새로 오픈한 프로그래머스 강남 교육장!! 모니터도 있고 좋은데 화장실이 좀...."
				, dayToVisit, 4000, 1L
		);

		@Test
		@DisplayName("성공적으로 등록한다")
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
}
