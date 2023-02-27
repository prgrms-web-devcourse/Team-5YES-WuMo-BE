package org.prgrms.wumo.domain.route.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
import org.prgrms.wumo.domain.route.dto.request.RouteRegisterRequest;
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
			.address("제주시 서귀포시 서귀동")
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
			.isPublic(false)
			.locations(new ArrayList<>() {{
				add(getLocationData());
			}})
			.party(getPartyData())
			.build();
	}
}
