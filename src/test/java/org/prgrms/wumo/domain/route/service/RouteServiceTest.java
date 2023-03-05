package org.prgrms.wumo.domain.route.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.wumo.domain.location.model.Category;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.party.repository.PartyRepository;
import org.prgrms.wumo.domain.route.dto.request.RouteGetAllRequest;
import org.prgrms.wumo.domain.route.dto.request.RouteRegisterRequest;
import org.prgrms.wumo.domain.route.dto.request.RouteStatusUpdateRequest;
import org.prgrms.wumo.domain.route.dto.request.SortType;
import org.prgrms.wumo.domain.route.dto.response.RouteGetAllResponses;
import org.prgrms.wumo.domain.route.dto.response.RouteGetResponse;
import org.prgrms.wumo.domain.route.dto.response.RouteRegisterResponse;
import org.prgrms.wumo.domain.route.model.Route;
import org.prgrms.wumo.domain.route.repository.RouteRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
@DisplayName("RouteService의 ")
public class RouteServiceTest {

	private final long routeId = 1L;
	private final long locationId = 1L;
	private final long partyId = 1L;

	@InjectMocks
	RouteService routeService;

	@Mock
	RouteRepository routeRepository;

	@Mock
	PartyRepository partyRepository;

	@Mock
	LocationRepository locationRepository;

	@Mock
	PartyMemberRepository partyMemberRepository;

	@BeforeEach
	void setUp() {
		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
			new UsernamePasswordAuthenticationToken(1L, null, Collections.EMPTY_LIST);

		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Nested
	@DisplayName("registerRoute 메소드는 루트에 후보지 등록 요청 시 ")
	class RegisterRoute {
		//given
		Party party = getPartyData();
		Location location = getLocationData();
		Route route = getRouteData();

		@Test
		@DisplayName("모임의 루트가 이미 있다면 그 루트에 후보지를 추가한다")
		void success_not_first_register() {
			//given
			RouteRegisterRequest routeRegisterRequest
				= new RouteRegisterRequest(routeId, locationId, partyId);

			//mocking
			given(routeRepository.findById(anyLong()))
				.willReturn(Optional.of(route));
			given(partyRepository.findById(anyLong()))
				.willReturn(Optional.of(party));
			given(locationRepository.findById(anyLong()))
				.willReturn(Optional.of(location));
			given(partyMemberRepository.existsByPartyIdAndMemberId(anyLong(), anyLong()))
				.willReturn(true);

			//when
			RouteRegisterResponse routeRegisterResponse = routeService.registerRoute(routeRegisterRequest);

			//then
			assertThat(routeRegisterResponse.id()).isEqualTo(routeId);
			then(routeRepository)
				.should()
				.findById(anyLong());
		}

		@Test
		@DisplayName("모임의 루트가 처음 생성되는거라면 루트 생성 후, 후보지를 루트에 추가한다")
		void success_first_register() {
			//given
			RouteRegisterRequest routeRegisterRequest
				= new RouteRegisterRequest(null, locationId, partyId);

			//mocking
			given(routeRepository.save(any(Route.class)))
				.willReturn(route);
			given(partyRepository.findById(anyLong()))
				.willReturn(Optional.of(party));
			given(locationRepository.findById(anyLong()))
				.willReturn(Optional.of(location));
			given(partyMemberRepository.existsByPartyIdAndMemberId(anyLong(), anyLong()))
				.willReturn(true);

			//when
			RouteRegisterResponse routeRegisterResponse = routeService.registerRoute(routeRegisterRequest);

			//then
			assertThat(routeRegisterResponse.id()).isEqualTo(routeId);
			then(routeRepository)
				.should()
				.save(any(Route.class));
		}

		@Test
		@DisplayName("모임의 회원이 아니라면 예외가 발생한다")
		void fail_not_party_member() {
			//given
			RouteRegisterRequest routeRegisterRequest
				= new RouteRegisterRequest(routeId, locationId, partyId);

			//mocking
			given(partyRepository.findById(anyLong()))
				.willReturn(Optional.of(party));
			given(locationRepository.findById(anyLong()))
				.willReturn(Optional.of(location));
			given(partyMemberRepository.existsByPartyIdAndMemberId(anyLong(), anyLong()))
				.willReturn(false);

			//when, then
			assertThatThrownBy(() -> routeService.registerRoute(routeRegisterRequest))
				.isInstanceOf(AccessDeniedException.class)
				.hasMessage("잘못된 접근입니다.");
		}
	}

	@Nested
	@DisplayName("getRoute 메소드는 루트 상세 조회 요청 시 ")
	class GetRoute {
		//given
		Route route = getRouteData();

		@Test
		@DisplayName("모임에서 접근한 경우 해당 모임 멤버가 아니라면 예외가 발생한다")
		void success_in_party() {
			//given
			int isPublic = 0;

			//mocking
			given(routeRepository.findById(anyLong()))
				.willReturn(Optional.of(route));

			//when, then
			assertThatThrownBy(() -> routeService.getRoute(routeId, isPublic))
				.isInstanceOf(AccessDeniedException.class)
				.hasMessage("잘못된 접근입니다.");
		}

		@Test
		@DisplayName("공개 목록에서 접근한 경우 바로 루트 상세 정보를 반환한다")
		void success_from_public() {
			//given
			SecurityContextHolder.clearContext();
			int isPublic = 1;

			//mocking
			given(routeRepository.findById(anyLong()))
				.willReturn(Optional.of(route));

			//when
			RouteGetResponse result = routeService.getRoute(routeId, isPublic);

			//then
			assertThat(result.partyId()).isEqualTo(partyId);
			assertThat(result.locations()).hasSize(1);
			then(routeRepository)
				.should()
				.findById(anyLong());
		}
	}

	@Nested
	@DisplayName("getAllRoute 메소드는 공개 루트 목록 조회 요청 시 ")
	class GetAllRoute {
		//given
		List<Route> routes;

		@Test
		@DisplayName("공개 루트 목록을 최신순으로 반환한다")
		void success() {
			//given
			RouteGetAllRequest routeGetAllRequest
				= new RouteGetAllRequest(3L, 5, SortType.NEWEST, null);

			routes = List.of(getPublicRouteData2(), getPublicRouteData1());

			//mocking
			given(routeRepository.findAllByCursorAndSearchWord(any(), anyInt(), any(SortType.class), any()))
				.willReturn(routes);

			//when
			RouteGetAllResponses result = routeService.getAllRoute(routeGetAllRequest);

			//then
			assertThat(result.routes()).hasSize(2);
			assertThat(result.lastId()).isEqualTo(1);
		}

		@Test
		@DisplayName("검색어가 있는 경우 조건에 맞는 공개 루트 목록을 최신순으로 반환한다")
		void success_search() {
			//given
			RouteGetAllRequest routeGetAllRequest
				= new RouteGetAllRequest(3L, 5, SortType.NEWEST, "제주");

			routes = List.of(getPublicRouteData1());

			//mocking
			given(routeRepository.findAllByCursorAndSearchWord(any(), anyInt(), any(SortType.class), anyString()))
				.willReturn(routes);

			//when
			RouteGetAllResponses result = routeService.getAllRoute(routeGetAllRequest);

			//then
			assertThat(result.routes()).hasSize(1);
			assertThat(result.lastId()).isEqualTo(1);
		}

		@Test
		@DisplayName("조건에 만족하는 데이터가 없을 경우 빈 리스트를 반환한다")
		void success_empty_data() {
			//given
			RouteGetAllRequest routeGetAllRequest
				= new RouteGetAllRequest(3L, 5, SortType.NEWEST, "부산광역시");

			routes = Collections.emptyList();

			//mocking
			given(routeRepository.findAllByCursorAndSearchWord(any(), anyInt(), any(SortType.class), anyString()))
				.willReturn(routes);

			//when
			RouteGetAllResponses result = routeService.getAllRoute(routeGetAllRequest);

			//then
			assertThat(result.routes()).hasSize(0);
			assertThat(result.lastId()).isEqualTo(-1);
		}
	}

	@Nested
	@DisplayName("updateRoutePublicStatus 메소드는 루트 공개여부 변경 요청 시 ")
	class UpdateRoutePublicStatus {
		//given
		RouteStatusUpdateRequest routeStatusUpdateRequest
			= new RouteStatusUpdateRequest(routeId, true, "퇴사 기념 여행");

		Route route = getRouteData();

		@Test
		@DisplayName("요청한 회원이 해당 모임멤버인지 확인 후 변경한다")
		void success() {
			//mocking
			given(routeRepository.findById(anyLong()))
				.willReturn(Optional.of(route));
			given(partyMemberRepository.existsByPartyIdAndMemberId(anyLong(), anyLong()))
				.willReturn(true);

			//when
			routeService.updateRoutePublicStatus(routeStatusUpdateRequest);

			//then
			then(routeRepository)
				.should()
				.findById(anyLong());
		}
	}

	private Party getPartyData() {
		return Party.builder()
			.id(partyId)
			.name("제주도 한달 살기")
			.coverImage("http://~~~.png")
			.startDate(LocalDateTime.now())
			.endDate(LocalDateTime.now().plusDays(1))
			.build();
	}

	private Location getLocationData() {
		return Location.builder()
			.id(locationId)
			.name("오예스 찜닭")
			.latitude(12.3456F)
			.longitude(34.5678F)
			.address("제주특별자치도 서귀포시 서귀동")
			.image("http://~~~.png")
			.visitDate(LocalDateTime.now().plusDays(10))
			.category(Category.MEAL)
			.expectedCost(40000)
			.partyId(partyId)
			.build();
	}

	private Route getRouteData() {
		return Route.builder()
			.id(routeId)
			.locations(new ArrayList<>() {{
				add(getLocationData());
			}})
			.party(getPartyData())
			.build();
	}

	//제주시에 위치한 후보지가 포함된 루트
	private Route getPublicRouteData1() {
		Route route = getRouteData();
		route.updatePublicStatus(true);
		return route;
	}

	//서울특별시에 위치한 후보지가 포함된 루트
	private Route getPublicRouteData2() {
		Location location = Location.builder()
			.id(2L)
			.name("오예스 치킨")
			.latitude(23.3456F)
			.longitude(45.5678F)
			.address("서울특별시 강남구 테헤란로")
			.searchAddress("서울특별시")
			.image("http://~~~.png")
			.visitDate(LocalDateTime.now().plusDays(5))
			.category(Category.MEAL)
			.expectedCost(50000)
			.partyId(2L)
			.build();

		Route route = Route.builder()
			.id(2L)
			.locations(new ArrayList<>() {{
				add(location);
			}})
			.party(getPartyData())
			.build();
		route.updatePublicStatus(true);
		return route;
	}
}
