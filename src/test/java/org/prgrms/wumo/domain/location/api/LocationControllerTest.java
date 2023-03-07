package org.prgrms.wumo.domain.location.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.wumo.MysqlTestContainer;
import org.prgrms.wumo.domain.location.LocationTestUtils;
import org.prgrms.wumo.domain.location.dto.request.LocationRegisterRequest;
import org.prgrms.wumo.domain.location.model.Category;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.party.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("LocationController 를 통해 ")
public class LocationControllerTest extends MysqlTestContainer {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	LocationRepository locationRepository;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	PartyRepository partyRepository;

	@Autowired
	PartyMemberRepository partyMemberRepository;

	@Autowired
	private ObjectMapper objectMapper;

	// GIVEN
	LocationTestUtils locationTestUtils = new LocationTestUtils();

	Member member;
	Party party;
	PartyMember partyMember;

	@BeforeEach
	void beforeEach() {
		member = memberRepository.save(
				Member.builder()
						.password("qwe12345")
						.email("member@email.com")
						.nickname("nickname")
						.build()
		);

		party = partyRepository.save(
				Party.builder()
						.description("오예스팀 모임")
						.coverImage("party_cover_image.png")
						.name("오예스")
						.startDate(LocalDateTime.now().plusDays(2))
						.endDate(LocalDateTime.now().plusDays(5))
						.build()
		);

		partyMember = partyMemberRepository.save(
				PartyMember.builder()
						.member(member)
						.party(party)
						.role("총무")
						.isLeader(true)
						.build()
		);

		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(member.getId(), null, Collections.EMPTY_LIST);

		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	@AfterEach
	void afterEach() {
		partyMemberRepository.deleteById(partyMember.getId());
		partyRepository.deleteById(party.getId());
		memberRepository.deleteById(member.getId());

		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("후보지를 등록한다")
	void registerLocationTest() throws Exception {
		// Given
		LocationRegisterRequest locationRegisterRequest
				= new LocationRegisterRequest(
				"프로그래머스 강남 교육장",
				"서울특별시",
				"강남역 2번출구 올라가기 전",
				locationTestUtils.getLatitude1(),
				locationTestUtils.getLongitude1(),
				"http://programmers_gangnam_image.com",
				Category.STUDY,
				"이번에 새로 오픈한 프로그래머스 강남 교육장!! 모니터도 있고 좋은데 화장실이 좀....",
				locationTestUtils.getDayToVisit(),
				4000,
				party.getId()
		);

		// When
		ResultActions resultActions =
				mockMvc.perform(
						post("/api/v1/locations")
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.characterEncoding("UTF-8")
								.content(
										objectMapper.writeValueAsString(locationRegisterRequest)
								)
				);

		// Then
		resultActions
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andDo(print());

	}

	@Test
	@DisplayName("후보 장소 하나를 상세 조회할 수 있다.")
	void getLocationTest() throws Exception {
		// Given
		Long locationId = locationRepository.save(locationTestUtils.getLocation()).getId();

		// When
		ResultActions resultActions = mockMvc.perform(get("/api/v1/locations/{locationId}", locationId));

		// Then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.name").value("프로그래머스 강남 교육장"))
				.andExpect(jsonPath("$.latitude").value(locationTestUtils.getLatitude1()))
				.andExpect(jsonPath("$.longitude").value(locationTestUtils.getLongitude1()))
				.andDo(print());
	}

	@Test
	@DisplayName(" 특정 모임 내의 후보 장소 전체를 조회할 수 있다.")
	void getAllLocationTest() throws Exception {
		// Given
		List<Location> locations = locationRepository.saveAll(locationTestUtils.getLocations());
		Location firstLocation = locations.get(0);

		// When
		ResultActions resultActions = mockMvc.perform(
				get("/api/v1/locations")
						.param("cursorId", (String)null)
						.param("pageSize", "5")
						.param("partyId", "1")
		);

		// Then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.locations").isArray())
				.andExpect(jsonPath("$.locations").isNotEmpty())
				.andExpect(jsonPath("$.locations[0].id").isNotEmpty())
				.andDo(print());
	}

	@Test
	@DisplayName(" 루트에서 후보지를 삭제한다")
	void deleteRouteLocationTest() throws Exception {
		// Given
		Location location = locationRepository.save(
				Location.builder()
						.category(Category.COFFEE)
						.visitDate(locationTestUtils.getDayToVisit())
						.description("아인슈페너가 맛있는 곳!")
						.name("cafe")
						.searchAddress("고양시")
						.address("경기도 고양시 일산서구")
						.latitude(12.34F)
						.longitude(34.56F)
						.partyId(party.getId())
						.expectedCost(4000)
						.spending(3500)
						.image("image.url")
						.build()
		);

		Long locationId = locationRepository.save(location).getId();

		// When
		ResultActions resultActions = mockMvc.perform(
				delete("/api/v1/locations")
						.param("locationId", String.valueOf(locationId))
		);

		// Then
		resultActions
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	@DisplayName(" 후보지를 삭제한다.")
	void deleteLocation() throws Exception {
		// Given
		Long locationId = locationRepository.save(
				Location.builder()
						.category(Category.COFFEE)
						.visitDate(locationTestUtils.getDayToVisit())
						.description("아인슈페너가 맛있는 곳!")
						.name("cafe")
						.searchAddress("고양시")
						.address("경기도 고양시 일산서구")
						.latitude(12.34F)
						.longitude(34.56F)
						.partyId(party.getId())
						.expectedCost(4000)
						.spending(3500)
						.image("image.url")
						.build()
		).getId();

		// When
		ResultActions resultActions =
				mockMvc.perform(
						delete("/api/v1/locations/{locationId}", locationId)
				);

		// Then
		resultActions
				.andExpect(status().isOk())
				.andDo(print());
	}
}
