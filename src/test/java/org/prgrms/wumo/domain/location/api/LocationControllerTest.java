package org.prgrms.wumo.domain.location.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import org.prgrms.wumo.domain.location.dto.request.LocationSpendingUpdateRequest;
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
@DisplayName("LocationController ??? ?????? ")
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
	Location location;

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
						.description("???????????? ??????")
						.coverImage("party_cover_image.png")
						.name("?????????")
						.startDate(LocalDateTime.now().plusDays(2))
						.endDate(LocalDateTime.now().plusDays(5))
						.build()
		);

		partyMember = partyMemberRepository.save(
				PartyMember.builder()
						.member(member)
						.party(party)
						.role("??????")
						.isLeader(true)
						.build()
		);

		location = locationRepository.save(
				Location.builder()
						.image("http://programmers_gangnam_image.com")
						.memberId(member.getId())
						.description("????????? ?????? ????????? ?????????????????? ?????? ?????????!! ???????????? ?????? ????????? ???????????? ???....")
						.latitude(123.456).longitude(456.789)
						.address("????????? 2?????????").visitDate(LocalDateTime.now().plusDays(30))
						.searchAddress("???????????????")
						.category(Category.CULTURE).name("?????????????????? ?????? ?????????")
						.spending(3000).expectedCost(4000)
						.partyId(party.getId())
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
	@DisplayName("???????????? ????????????")
	void registerLocationTest() throws Exception {
		// Given
		LocationRegisterRequest locationRegisterRequest
				= new LocationRegisterRequest(
				"?????????????????? ?????? ?????????",
				"???????????????",
				"????????? 2????????? ???????????? ???",
				locationTestUtils.getLatitude1(),
				locationTestUtils.getLongitude1(),
				"http://programmers_gangnam_image.com",
				Category.CULTURE,
				"????????? ?????? ????????? ?????????????????? ?????? ?????????!! ???????????? ?????? ????????? ???????????? ???....",
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
	@DisplayName("?????? ?????? ????????? ?????? ????????? ??? ??????.")
	void getLocationTest() throws Exception {
		// Given
		//Long locationId = locationRepository.save(locationTestUtils.getLocation()).getId();

		// When
		ResultActions resultActions = mockMvc.perform(get("/api/v1/locations/{locationId}", location.getId()));

		// Then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andDo(print());
	}

	@Test
	@DisplayName(" ?????? ?????? ?????? ?????? ?????? ????????? ????????? ??? ??????.")
	void getAllLocationTest() throws Exception {
		// Given
		List<Location> locations = locationRepository.saveAll(locationTestUtils.getLocations());

		// When
		ResultActions resultActions = mockMvc.perform(
				get("/api/v1/locations")
						.param("cursorId", (String)null)
						.param("pageSize", "5")
						.param("partyId", String.valueOf(party.getId()))
		);

		// Then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.locations").isArray())
				.andExpect(jsonPath("$.locations").isNotEmpty())
				.andExpect(jsonPath("$.locations[0].id").isNotEmpty())
				.andExpect(jsonPath("$.lastId").isNotEmpty())
				.andDo(print());
	}

	@Test
	@DisplayName(" ???????????? ???????????? ????????????")
	void deleteRouteLocationTest() throws Exception {
		// Given
		Location location = locationRepository.save(
				Location.builder()
						.memberId(member.getId())
						.category(Category.COFFEE)
						.visitDate(locationTestUtils.getDayToVisit())
						.description("?????????????????? ????????? ???!")
						.name("cafe")
						.searchAddress("?????????")
						.address("????????? ????????? ????????????")
						.latitude(12.34)
						.longitude(34.56)
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
	@DisplayName(" ???????????? ????????????.")
	void deleteLocation() throws Exception {
		// Given
		Long locationId = locationRepository.save(
				Location.builder()
						.memberId(member.getId())
						.category(Category.COFFEE)
						.visitDate(locationTestUtils.getDayToVisit())
						.description("?????????????????? ????????? ???!")
						.name("cafe")
						.searchAddress("?????????")
						.address("????????? ????????? ????????????")
						.latitude(12.34)
						.longitude(34.56)
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

	@Test
	@DisplayName("??????????????? ?????? ????????? ????????? ????????? ??? ??????.")
	void updateSpending() throws Exception {
		// Given
		Location location = locationRepository.save(
				Location.builder()
						.memberId(member.getId())
						.category(Category.COFFEE)
						.visitDate(locationTestUtils.getDayToVisit())
						.description("?????????????????? ????????? ???!")
						.name("cafe")
						.searchAddress("?????????")
						.address("????????? ????????? ????????????")
						.latitude(12.34)
						.longitude(34.56)
						.partyId(party.getId())
						.expectedCost(4000)
						.spending(3500)
						.image("image.url")
						.build()
		);

		LocationSpendingUpdateRequest locationSpendingUpdateRequest =
				new LocationSpendingUpdateRequest(location.getId(), 50000);

		// When
		ResultActions resultActions =
				mockMvc.perform(
						patch("/api/v1/locations/spending")
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.characterEncoding("UTF-8")
								.content(
										objectMapper.writeValueAsString(locationSpendingUpdateRequest)
								)
				);

		// Then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.spending").value("50000"))
				.andDo(print());

	}
}
