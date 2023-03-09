package org.prgrms.wumo.domain.route.api;

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
import org.prgrms.wumo.domain.like.model.RouteLike;
import org.prgrms.wumo.domain.like.repository.RouteLikeRepository;
import org.prgrms.wumo.domain.location.model.Category;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.party.repository.PartyRepository;
import org.prgrms.wumo.domain.route.dto.request.RouteRegisterRequest;
import org.prgrms.wumo.domain.route.dto.request.RouteStatusUpdateRequest;
import org.prgrms.wumo.domain.route.model.Route;
import org.prgrms.wumo.domain.route.repository.RouteRepository;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("RouteController를 통해 ")
public class RouteControllerTest extends MysqlTestContainer {

	private long memberId;
	private long partyId;
	private long locationId;
	private long partyMemberId;
	private long routeId;
	private long routeLikeId;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	PartyRepository partyRepository;

	@Autowired
	LocationRepository locationRepository;

	@Autowired
	PartyMemberRepository partyMemberRepository;

	@Autowired
	RouteRepository routeRepository;

	@Autowired
	RouteLikeRepository routeLikeRepository;

	@BeforeEach
	void setup() {
		Member member = memberRepository.save(getMemberData());
		memberId = member.getId();

		Party party = partyRepository.save(getPartyData());
		partyId = party.getId();

		Location location = locationRepository.save(getLocationData());
		locationId = location.getId();

		PartyMember partyMember = partyMemberRepository.save(getPartyMemberData(member, party));
		partyMemberId = partyMember.getId();

		Route route = routeRepository.save(getRouteData(location, party));
		routeId = route.getId();
		route.updatePublicStatus("퇴사 기념 제주도 한달 살기", true);
		routeRepository.save(route);

		RouteLike routeLike = routeLikeRepository.save(getRouteLikeData(member, route));
		routeLikeId = routeLike.getId();

		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
			new UsernamePasswordAuthenticationToken(memberId, null, Collections.EMPTY_LIST);

		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	@AfterEach
	void tearDown() {
		routeLikeRepository.deleteById(routeLikeId);
		routeRepository.deleteById(routeId);
		partyMemberRepository.deleteById(partyMemberId);
		locationRepository.deleteById(locationId);
		partyRepository.deleteById(partyId);
		memberRepository.deleteById(memberId);
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("후보지를 루트에 등록한다")
	void register_route() throws Exception {
		//given
		RouteRegisterRequest routeRegisterRequest
			= new RouteRegisterRequest(null, locationId, partyId);

		//when
		ResultActions resultActions
			= mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/routes")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(objectMapper.writeValueAsString(routeRegisterRequest)));

		//then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andDo(print());
	}

	@Test
	@DisplayName("모임 안에서 루트를 상세 조회 한다")
	void get_route_in_party() throws Exception {
		//when
		ResultActions resultActions
			= mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/routes/{partyId}", partyId)
			.param("path", "0"));

		//then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(routeId))
			.andExpect(jsonPath("$.isPublic").value(true))
			.andExpect(jsonPath("$.locations").isArray())
			.andExpect(jsonPath("$.partyId").value(partyId))
			.andDo(print());
	}

	@Test
	@DisplayName("공개된 루트 목록에서 루트를 상세 조회 한다")
	void get_route_from_public_list() throws Exception {
		//when
		ResultActions resultActions
			= mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/routes/{partyId}", partyId)
			.param("path", "1"));

		//then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(routeId))
			.andExpect(jsonPath("$.isPublic").isNotEmpty())
			.andExpect(jsonPath("$.locations").isArray())
			.andExpect(jsonPath("$.partyId").value(partyId))
			.andDo(print());
	}

	@Test
	@DisplayName("공개된 루트 목록을 조회 한다")
	void get_all_route() throws Exception {
		//given
		int pageSize = 5;
		String sortType = "NEWEST";
		String searchWord = null;

		//when
		ResultActions resultActions
			= mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/routes")
			.param("cursorId", (String)null)
			.param("pageSize", String.valueOf(pageSize))
			.param("sortType", sortType)
			.param("searchWord", searchWord));

		//then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.lastId").value(routeId))
			.andExpect(jsonPath("$.routes").isArray())
			.andExpect(jsonPath("$.routes[0].routeId").value(routeId))
			.andDo(print());
	}

	@Test
	@DisplayName("관심 루트 목록을 조회 한다")
	void get_all_liked_route() throws Exception {
		//given
		Long cursorId = null;
		int pageSize = 5;
		String sortType = "NEWEST";
		String searchWord = null;

		//when
		ResultActions resultActions
			= mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/routes/likes")
			.param("cursorId", (String)null)
			.param("pageSize", String.valueOf(pageSize))
			.param("sortType", sortType)
			.param("searchWord", searchWord));

		//then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.lastId").value(routeLikeId))
			.andExpect(jsonPath("$.routes").isArray())
			.andExpect(jsonPath("$.routes[0].routeId").value(routeId))
			.andDo(print());
	}

	@Test
	@DisplayName("루트의 공개여부를 공개로 변경한다")
	void update_route_public_status() throws Exception {
		//given
		RouteStatusUpdateRequest routeStatusUpdateRequest
			= new RouteStatusUpdateRequest(routeId, true, "퇴사 기념 여행");

		//when
		ResultActions resultActions
			= mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/routes")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(objectMapper.writeValueAsString(routeStatusUpdateRequest)));

		//then
		resultActions
			.andExpect(status().isOk());
	}

	private Member getMemberData() {
		return Member.builder()
			.email("taehee@gmail.com")
			.nickname("태희")
			.password("qwe12345")
			.build();
	}

	private Party getPartyData() {
		return Party.builder()
			.name("제주도 한달 살기")
			.coverImage("http://~~~.png")
			.startDate(LocalDateTime.now())
			.endDate(LocalDateTime.now().plusDays(1))
			.build();
	}

	private Location getLocationData() {
		return Location.builder()
			.name("오예스 찜닭")
			.memberId(memberId)
			.latitude(12.3456)
			.longitude(34.5678)
			.searchAddress("서귀포시")
			.address("제주특별자치도 서귀포시 서귀동")
			.image("http://~~~.png")
			.visitDate(LocalDateTime.now().plusDays(10))
			.category(Category.MEAL)
			.expectedCost(40000)
			.partyId(partyId)
			.build();
	}

	private PartyMember getPartyMemberData(Member member, Party party) {
		return PartyMember.builder()
			.member(member)
			.party(party)
			.role("총무")
			.isLeader(true)
			.build();
	}

	private Route getRouteData(Location location, Party party) {
		return Route.builder()
			.locations(List.of(location))
			.party(party)
			.build();
	}

	private RouteLike getRouteLikeData(Member member, Route route) {
		return RouteLike.builder()
			.memberId(member.getId())
			.routeId(route.getId())
			.build();
	}
}
