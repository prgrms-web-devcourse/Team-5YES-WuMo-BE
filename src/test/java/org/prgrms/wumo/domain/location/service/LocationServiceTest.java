package org.prgrms.wumo.domain.location.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.prgrms.wumo.domain.location.mapper.LocationMapper.toLocationGetResponse;

import java.time.LocalDateTime;
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
import org.prgrms.wumo.domain.location.dto.request.LocationSpendingUpdateRequest;
import org.prgrms.wumo.domain.location.dto.request.LocationUpdateRequest;
import org.prgrms.wumo.domain.location.dto.response.LocationGetAllResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationGetResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationRegisterResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationSpendingUpdateResponse;
import org.prgrms.wumo.domain.location.dto.response.LocationUpdateResponse;
import org.prgrms.wumo.domain.location.model.Category;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
@DisplayName("LocationService ?????? ")
public class LocationServiceTest {

	@InjectMocks
	LocationService locationService;

	@Mock
	LocationRepository locationRepository;

	@Mock
	PartyMemberRepository partyMemberRepository;

	// GIVEN
	LocationTestUtils locationTestUtils = new LocationTestUtils();

	Member leader, member;
	Party party;
	PartyMember leaderPartyMember, normalPartyMember;

	@BeforeEach
	void beforeEach() {
		leader = Member.builder()
				.id(1L)
				.password("qwe12345")
				.email("wumo@email.com")
				.nickname("DaRamG")
				.build();
		member = Member.builder()
				.id(2L)
				.password("qwe12345")
				.email("wumo@gmail.com")
				.nickname("HoRangE")
				.build();
		party = Party.builder()
				.id(1L)
				.description("???????????? ??????")
				.coverImage("party_cover_image.png")
				.name("?????????")
				.endDate(LocalDateTime.now().plusDays(5))
				.startDate(LocalDateTime.now().plusDays(2))
				.build();
		leaderPartyMember = PartyMember.builder()
				.id(1L)
				.member(leader)
				.party(party)
				.role("??????")
				.isLeader(true)
				.build();
		normalPartyMember = PartyMember.builder()
				.id(2L)
				.member(member)
				.party(party)
				.role("??????")
				.isLeader(false)
				.build();

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
	@DisplayName("registerLocation??? ???????????? ")
	class RegisterLocation {
		// Given
		LocationRegisterRequest locationRegisterRequest
				= new LocationRegisterRequest(
				"?????????????????? ?????? ?????????", "???????????????", "????????? 2?????????"
				, locationTestUtils.getLatitude1(), locationTestUtils.getLongitude1(),
				"http://programmers_gangnam_image.com"
				, Category.CULTURE, "????????? ?????? ????????? ?????????????????? ?????? ?????????!! ???????????? ?????? ????????? ???????????? ???...."
				, locationTestUtils.getDayToVisit(), 4000, 1L
		);

		@Test
		@DisplayName("?????? ????????? ????????? ??? ??????.")
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
	@DisplayName("getLocation??? ???????????? ")
	class GetLocation {
		// Given

		@Test
		@DisplayName("?????? ?????? ????????? ?????? ????????? ??? ??????.")
		void getOneLocationTest() {
			// Given
			given(locationRepository.findById(1L)).willReturn(Optional.of(locationTestUtils.getLocation()));
			given(partyMemberRepository.findByPartyIdAndMemberId(any(Long.class), any(Long.class))).willReturn(
					Optional.of(leaderPartyMember));

			// When
			LocationGetResponse locationGetResponse =
					locationService.getLocation(1L);

			// Then
			assertThat(locationGetResponse).usingRecursiveComparison()
					.isEqualTo(toLocationGetResponse(locationTestUtils.getLocation(), true));
		}

		@Test
		@DisplayName(" ?????? ?????? ????????? ?????? ????????? ??? ??????.")
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
	@DisplayName("getAllLocation??? ???????????? ")
	class getAllLocations {
		// Given
		int pageSize = 5;
		Long partyId = 1L;

		LocationGetAllRequest locationGetAllRequest
				= new LocationGetAllRequest(1L, pageSize, partyId);

		List<Location> partyId1Locations = new ArrayList<>();

		@Test
		@DisplayName("?????? ????????? ?????? ?????? ????????? ????????? ??? ??????.")
		void getAllLocationsTest() {
			// Given
			for (Location location : locationTestUtils.getLocations()) {
				if (partyId1Locations.size() >= 5)
					break;
				if (location.getPartyId() == 1L)
					partyId1Locations.add(location);
			}

			given(locationRepository.findByPartyId(1L, 5, 1L))
					.willReturn(partyId1Locations);
			given(partyMemberRepository.findByPartyIdAndMemberId(any(Long.class), any(Long.class))).willReturn(
					Optional.of(leaderPartyMember));

			// When
			LocationGetAllResponse locationGetAllResponse = locationService.getAllLocation(locationGetAllRequest);

			// Then
			assertThat(locationGetAllResponse.locations().size()).isEqualTo(5);
			for (int i = 0; i < 4; i++) {
				assertThat(locationGetAllResponse.locations().get(i)).usingRecursiveComparison()
						.isEqualTo(toLocationGetResponse(locationTestUtils.getLocations().get(i), true));
			}
			assertThat(locationGetAllResponse.locations().get(4)).usingRecursiveComparison()
					.isEqualTo(toLocationGetResponse(locationTestUtils.getLocations().get(5), true));
		}

	}

	@Nested
	@DisplayName("deleteRouteLocation??? ???????????? ")
	class deleteRouteLocation {
		// Given
		Long locationId = locationTestUtils.getLocation().getId();

		@Test
		@DisplayName("???????????? ???????????? ????????? ??? ??????.")
		void deleteRouteLocationTest() {
			given(locationRepository.findById(any(Long.class))).willReturn(Optional.of(locationTestUtils.getLocation()));
			given(partyMemberRepository.findByPartyIdAndMemberId(any(Long.class), any(Long.class))).willReturn(Optional.of(
					leaderPartyMember));

			// When
			locationService.deleteRouteLocation(locationId);

			// Then
			then(locationRepository)
					.should()
					.findById(any(Long.class));
		}
	}

	@Nested
	@DisplayName("updateLocation??? ????????????")
	class updateLocation {
		// GIVEN
		Long locationId = locationTestUtils.getLocation().getId();
		LocalDateTime dayToVisit = locationTestUtils.getDayToVisit();

		LocationUpdateRequest locationUpdateRequest =
				new LocationUpdateRequest(locationId, "changed_image.png", Category.CULTURE, "????????? ?????? ??????", dayToVisit, 56000,
						1L);

		@Test
		@DisplayName("???????????? ????????? ????????? ??? ??????.")
		void success() {
			// Given
			given(locationRepository.findById(any(Long.class))).willReturn(Optional.of(locationTestUtils.getLocation()));
			given(partyMemberRepository.findByPartyIdAndMemberId(any(Long.class), any(Long.class))).willReturn(Optional.of(
					leaderPartyMember));

			LocationUpdateResponse expected =
					new LocationUpdateResponse(locationId, "changed_image.png", "????????? ?????? ??????", dayToVisit, 56000, Category.CULTURE);

			// When
			LocationUpdateResponse locationUpdateResponse = locationService.updateLocation(locationUpdateRequest);

			// Then
			assertThat(locationUpdateResponse).usingRecursiveComparison().isEqualTo(expected);
		}

		@Test
		@DisplayName(" ????????? ?????? ???????????? ???????????? ???????????? ????????? ???????????? ????????? ??? ??????.")
		void failedByUnAuthorized() {
			// Given
			given(locationRepository.findById(any(Long.class))).willReturn(Optional.of(locationTestUtils.getLocation()));
			given(partyMemberRepository.findByPartyIdAndMemberId(any(Long.class), any(Long.class))).willReturn(Optional.of(
					normalPartyMember));

			// When // Then
			assertThatThrownBy(
					() -> locationService.updateLocation(locationUpdateRequest)
			).isInstanceOf(AccessDeniedException.class);
		}
	}

	@Nested
	@DisplayName("deleteLocation??? ???????????? ")
	class deleteLocation {
		// Given
		Long locationId = locationTestUtils.getLocation().getId();

		@Test
		@DisplayName("???????????? ????????? ??? ??????.")
		void success() {
			// Given
			given(locationRepository.findById(any(Long.class))).willReturn(Optional.of(locationTestUtils.getLocation()));
			given(partyMemberRepository.findByPartyIdAndMemberId(any(Long.class), any(Long.class))).willReturn(Optional.of(
					leaderPartyMember));

			// When
			locationService.deleteLocation(locationId);

			// Then
			then(locationRepository).should().findById(locationId);
		}

		@Test
		@DisplayName("????????? ????????? ?????? ????????? ????????? ??? ??????.")
		void failInvalid() {
			// Given
			given(locationRepository.findById(any(Long.class))).willReturn(Optional.of(locationTestUtils.getLocation()));
			given(partyMemberRepository.findByPartyIdAndMemberId(any(Long.class), any(Long.class))).willReturn(Optional.of(
					normalPartyMember));

			// When // Then
			assertThatThrownBy(
					() -> locationService.deleteLocation(locationId)
			).isInstanceOf(AccessDeniedException.class);
		}
	}

	@Nested
	@DisplayName("updateSpending??? ?????? ")
	class updateSpending {
		// GIVEN
		Long locationId = locationTestUtils.getLocation().getId();
		LocationSpendingUpdateRequest locationSpendingUpdateRequest =
				new LocationSpendingUpdateRequest(locationId, 45000);

		@Test
		@DisplayName("???????????? ?????? ?????? ????????? ????????? ??? ??????.")
		void success() {
			// Given
			given(locationRepository.findById(any(Long.class))).willReturn(Optional.of(locationTestUtils.getLocation()));
			given(partyMemberRepository.existsByPartyIdAndMemberId(any(Long.class), any(Long.class))).willReturn(true);

			// When
			LocationSpendingUpdateResponse locationSpendingUpdateResponse =
					locationService.updateSpending(locationSpendingUpdateRequest);

			// Then
			assertThat(locationSpendingUpdateResponse.spending()).isEqualTo(45000);
			then(locationRepository)
					.should()
					.save(any(Location.class));

		}

		@Test
		@DisplayName("????????? ???????????? ?????? ?????? ????????? ????????? ??? ??????.")
		void failedUnAuthorized() {
			// Given
			given(locationRepository.findById(any(Long.class))).willReturn(Optional.of(locationTestUtils.getLocation()));
			given(partyMemberRepository.existsByPartyIdAndMemberId(any(Long.class), any(Long.class))).willReturn(false);

			// When // Then
			assertThatThrownBy(
					() -> locationService.updateSpending(locationSpendingUpdateRequest)
			).isInstanceOf(AccessDeniedException.class);
		}
	}
}
