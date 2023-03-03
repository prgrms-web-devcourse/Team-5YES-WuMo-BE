package org.prgrms.wumo.domain.location.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.prgrms.wumo.global.mapper.LocationMapper.toLocationGetResponse;
import static org.prgrms.wumo.global.mapper.LocationMapper.toLocationUpdateResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.wumo.domain.location.LocationTestUtils;
import org.prgrms.wumo.domain.location.dto.request.LocationGetAllRequest;
import org.prgrms.wumo.domain.location.dto.request.LocationRegisterRequest;
import org.prgrms.wumo.domain.location.dto.request.LocationUpdateRequest;
import org.prgrms.wumo.domain.location.dto.response.LocationGetAllResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationGetResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationRegisterResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationUpdateResponse;
import org.prgrms.wumo.domain.location.model.Category;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
@DisplayName("LocationService 에서 ")
public class LocationServiceTest {

	@InjectMocks
	LocationService locationService;

	@Mock
	LocationRepository locationRepository;

	@Mock
	PartyMemberRepository partyMemberRepository;

	// GIVEN
	LocationTestUtils locationTestUtils = new LocationTestUtils();

	@BeforeEach
	void beforeEach() {
		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(1L, null, Collections.EMPTY_LIST);

		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	@AfterEach
	void afterEach() {
		SecurityContextHolder.clearContext();
	}

	@Nested
	@DisplayName("registerLocation을 사용해서 ")
	class RegisterLocation {
		// Given
		LocationRegisterRequest locationRegisterRequest
				= new LocationRegisterRequest(
				"프로그래머스 강남 교육장", "서울특별시 강남구", "강남역 2번출구"
				, locationTestUtils.getLatitude1(), locationTestUtils.getLongitude1(),
				"http://programmers_gangnam_image.com"
				, Category.STUDY, "이번에 새로 오픈한 프로그래머스 강남 교육장!! 모니터도 있고 좋은데 화장실이 좀...."
				, locationTestUtils.getDayToVisit(), 4000, 1L
		);

		@Test
		@DisplayName("후보 장소를 등록할 수 있다.")
		void registerLocationTest() {
			// Given
			given(locationRepository.save(any(Location.class))).willReturn(locationTestUtils.getLocation());
			given(partyMemberRepository.existsByPartyIdAndMemberId(any(Long.class), any(Long.class))).willReturn(true);

			// When
			LocationRegisterResponse locationRegisterResponse =
					locationService.registerLocation(locationRegisterRequest);

			// Then
			assertThat(locationRegisterResponse.id()).isEqualTo(1L);

		}
	}

	@Nested
	@DisplayName("getLocation을 사용해서 ")
	class GetLocation {
		// Given

		@Test
		@DisplayName("후보 장소 하나를 상세 조회할 수 있다.")
		void getOneLocationTest() {
			// Given
			given(locationRepository.findById(1L)).willReturn(Optional.of(locationTestUtils.getLocation()));

			// When
			LocationGetResponse locationGetResponse =
					locationService.getLocation(1L);

			// Then
			assertThat(locationGetResponse).usingRecursiveComparison()
					.isEqualTo(toLocationGetResponse(locationTestUtils.getLocation()));
		}

		@Test
		@DisplayName(" 없는 후보 장소는 상세 조회할 수 없다.")
		void getLocationFailTest() {
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
	class getAllLocations {
		// Given
		int pageSize = 5;
		Long partyId = 1L;

		LocationGetAllRequest locationGetAllRequest
				= new LocationGetAllRequest(1L, pageSize, partyId);

		List<Location> partyId1Locations = new ArrayList<>();

		@Test
		@DisplayName("해당 모임의 모든 후보 장소를 가져올 수 있다.")
		void getAllLocationsTest() {
			// Given
			for (Location location : locationTestUtils.getLocations()) {
				if (partyId1Locations.size() >= 5) {
					break;
				}
				if (location.getPartyId() == 1L) {
					partyId1Locations.add(location);
				}
			}

			given(locationRepository.findByPartyId(1L, 5, 1L))
					.willReturn(partyId1Locations);

			// When
			LocationGetAllResponse locationGetAllResponse = locationService.getAllLocation(locationGetAllRequest);

			// Then
			assertThat(locationGetAllResponse.locations().size()).isEqualTo(5);
			for (int i = 0; i < 4; i++) {
				assertThat(locationGetAllResponse.locations().get(i)).usingRecursiveComparison()
						.isEqualTo(toLocationGetResponse(locationTestUtils.getLocations().get(i)));
			}
			assertThat(locationGetAllResponse.locations().get(4)).usingRecursiveComparison()
					.isEqualTo(toLocationGetResponse(locationTestUtils.getLocations().get(5)));
		}

	}

	@Nested
	@DisplayName("updateLocation을 사용해서")
	class updateLocation {

		@Test
		@DisplayName("후보지의 정보를 변경할 수 있다.")
		void success() {
			// Given
			Location original = locationTestUtils.getLocation();
			Location edited = Location.builder()
					.id(1L).name("변경된 이름").description("변경된 상세 정보")
					.searchAddress("서울특별시 강남구")
					.category(Category.DRINK).longitude(45.56F).latitude(12.34F)
					.expectedCost(1234567).image("http://~image_edited.png")
					.visitDate(locationTestUtils.getDayToVisit())
					.address("변경된 주소")
					.partyId(1L)
					.build();

			LocationUpdateRequest updateRequest =
					new LocationUpdateRequest(1L, "변경된 이름", "서울특별시 강남구", "변경된 주소", 12.34F, 45.56F,
							"http://~image_edited.png", "변경된 상세 정보", locationTestUtils.getDayToVisit(),
							1234567, Category.DRINK, 1L
					);

			LocationUpdateResponse expected = toLocationUpdateResponse(edited);

			given(partyMemberRepository.existsByPartyIdAndMemberId(any(Long.class), any(Long.class))).willReturn(true);
			given(locationRepository.findById(any(Long.class))).willReturn(Optional.of(original));
			given(locationRepository.save(any(Location.class))).willReturn(edited);

			// When
			LocationUpdateResponse response = locationService.updateLocation(updateRequest);

			// Then
			assertThat(response).usingRecursiveComparison().isEqualTo(expected);
		}

	}

	@Nested
	@DisplayName("deleteRouteLocation을 사용해서 ")
	class deleteRouteLocation {
		// Given
		Long locationId = 1L;

		@Test
		@DisplayName("루트에서 후보지를 삭제할 수 있다.")
		void deleteRouteLocationTest() {
			given(locationRepository.findById(any(Long.class))).
					willReturn(Optional.of(locationTestUtils.getLocation()));

			given(partyMemberRepository.existsByPartyIdAndMemberId(any(Long.class), any(Long.class))).willReturn(true);

			// When
			locationService.deleteRouteLocation(locationId);

			// Then
			then(locationRepository)
					.should()
					.findById(any(Long.class));
		}
	}
}
