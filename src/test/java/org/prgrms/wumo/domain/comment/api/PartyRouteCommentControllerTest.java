package org.prgrms.wumo.domain.comment.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.model.PartyRouteComment;
import org.prgrms.wumo.domain.comment.repository.PartyRouteCommentRepository;
import org.prgrms.wumo.domain.location.model.Category;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.party.repository.PartyRepository;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("PartyRouteCommentController를 통해 ")
public class PartyRouteCommentControllerTest {

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

	// GIVEN
	Member member;
	Party party;
	PartyMember partyMember;
	Location location;
	Route route;

	List<Location> locations = new ArrayList<>();
	@Autowired
	private PartyRouteCommentRepository partyRouteCommentRepository;

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
						//.id(1L)
						.password("1234").description("오예스팀 모임")
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

		location = locationRepository.save(
				Location.builder()
						.category(Category.COFFEE)
						.visitDate(LocalDateTime.now().plusDays(4))
						.description("아인슈페너가 맛있는 곳!")
						.name("cafe")
						.address("경기도 고양시")
						.latitude(12.34F)
						.longitude(34.56F)
						.partyId(party.getId())
						.expectedCost(4000)
						.spending(3500)
						.image("image.url")
						.build()
		);

		locations.add(location);

		route = routeRepository.save(
				Route.builder()
						.party(party)
						.locations(locations)
						.build()
		);

		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(member.getId(), null, Collections.EMPTY_LIST);

		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	@AfterEach
	void afterEach() {
		routeRepository.deleteById(route.getId());
		locationRepository.deleteById(location.getId());
		partyMemberRepository.deleteById(partyMember.getId());
		partyRepository.deleteById(party.getId());
		memberRepository.deleteById(member.getId());

		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("모임 내 일정에 댓글을 생성할 수 있다")
	void registerPartyRouteCommentTest() throws Exception {
		// Given
		PartyRouteCommentRegisterRequest request = new PartyRouteCommentRegisterRequest(
				member.getId(), "모임 내 댓글", "image.png", route.getId(), location.getId()
		);

		// When
		ResultActions resultActions =
				mockMvc.perform(
						post("/api/v1/party-route-comments")
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.characterEncoding("UTF-8")
								.content(
										objectMapper.writeValueAsString(request)
								)
				);

		// Then
		resultActions
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andDo(print());
	}

	@Test
	@DisplayName("모임 내 루트에서 특정 후보지의 댓글을 목록 조회할 수 있다.")
	void getAllPartyRouteCommentTest() throws Exception {
		// Given
		PartyRouteComment partyRouteComment2 = PartyRouteComment.builder()
				.routeId(route.getId())
				.partyMember(partyMember)
				.image("image.png")
				.isEdited(false)
				.content("댓글 댓글 댓글")
				.locationId(1L)
				.member(member)
				.build();

		PartyRouteComment partyRouteComment3 = PartyRouteComment.builder()
				.routeId(route.getId())
				.partyMember(partyMember)
				.image("image.png")
				.isEdited(false)
				.content("댓글 댓글 댓글")
				.locationId(1L)
				.member(member)
				.build();

		partyRouteCommentRepository.saveAll(List.of(partyRouteComment2, partyRouteComment3));

		// When
		ResultActions resultActions =
				mockMvc.perform(
						get("/api/v1/party-route-comments")
								.param("cursorId", (String)null)
								.param("pageSize", "2")
								.param("locationId", "1")
				);

		// Then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.partyRouteComments").isNotEmpty())
				.andExpect(jsonPath("$.partyRouteComments").isArray())
				.andExpect(jsonPath("$.partyRouteComments[0].content").value(partyRouteComment2.getContent()))
				.andExpect(jsonPath("$.lastId").isNotEmpty())
				.andDo(print());
	}
}
