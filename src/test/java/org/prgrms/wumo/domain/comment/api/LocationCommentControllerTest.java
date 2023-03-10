package org.prgrms.wumo.domain.comment.api;

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
import org.prgrms.wumo.domain.comment.dto.request.LocationCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.request.LocationCommentUpdateRequest;
import org.prgrms.wumo.domain.comment.model.LocationComment;
import org.prgrms.wumo.domain.comment.repository.LocationCommentRepository;
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
@DisplayName("LocationCommentController??? ?????? ")
public class LocationCommentControllerTest extends MysqlTestContainer {

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
	LocationCommentRepository locationCommentRepository;

	// GIVEN
	Member member;
	Party party;
	PartyMember partyMember;
	Location location;
	LocationComment locationComment;

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
						.category(Category.COFFEE)
						.memberId(member.getId())
						.visitDate(LocalDateTime.now().plusDays(4))
						.description("?????????????????? ????????? ???!")
						.name("cafe")
						.address("????????? ????????? ????????????")
						.searchAddress("?????????")
						.latitude(12.34)
						.longitude(34.56)
						.partyId(party.getId())
						.expectedCost(4000)
						.spending(3500)
						.image("image.url")
						.build()
		);

		locationComment = locationCommentRepository.save(
				LocationComment.builder()
						.image("image.png")
						.content("?????? ??????")
						.locationId(location.getId())
						.partyMember(partyMember)
						.member(member)
						.build()
		);

		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(member.getId(), null, Collections.EMPTY_LIST);

		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	@AfterEach
	void afterEach() {
		locationCommentRepository.deleteById(locationComment.getId());
		locationRepository.deleteById(location.getId());
		partyMemberRepository.deleteById(partyMember.getId());
		partyRepository.deleteById(party.getId());
		memberRepository.deleteById(member.getId());

		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("????????? ????????? ????????? ??? ??????.")
	void registerLocationComment() throws Exception {
		// Given
		LocationCommentRegisterRequest locationCommentRegisterRequest =
				new LocationCommentRegisterRequest("?????? ??????", "image.png", location.getId(), party.getId());

		// When
		ResultActions resultActions =
				mockMvc.perform(
						post("/api/v1/location-comments")
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.characterEncoding("UTF-8")
								.content(
										objectMapper.writeValueAsString(locationCommentRegisterRequest)
								)
				);

		// Then
		resultActions
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andDo(print());
	}

	@Test
	@DisplayName("?????? ???????????? ?????? ????????? ????????? ??? ??????.")
	void getAllLocationCommentTest() throws Exception {
		// Given
		LocationComment locationComment1
				= LocationComment.builder()
				.image("image.png")
				.content("??? ?????? ??????")
				.locationId(location.getId())
				.partyMember(partyMember)
				.member(member)
				.build();

		LocationComment locationComment2 = LocationComment.builder()
				.image("image.png")
				.content("??? ?????? ??????")
				.locationId(location.getId())
				.partyMember(partyMember)
				.member(member)
				.build();

		locationCommentRepository.saveAll(List.of(locationComment1, locationComment2));

		// When
		ResultActions resultActions =
				mockMvc.perform(
						get("/api/v1/location-comments")
								.param("cursorId", (String)null)
								.param("pageSize", "2")
								.param("locationId", String.valueOf(location.getId()))
				);

		// Then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.locationComments").isArray())
				.andExpect(jsonPath("$.locationComments").isNotEmpty())
				.andExpect(jsonPath("$.lastId").isNotEmpty())
				.andDo(print());
	}

	@Test
	@DisplayName(" ????????? ????????? ????????? ??? ??????.")
	void updateLocationCommentUpdateTest() throws Exception {
		// Given
		LocationCommentUpdateRequest request =
				new LocationCommentUpdateRequest(locationComment.getId(), "???????????? ???????????? ?????????!!", "image.png");

		// When
		ResultActions resultActions =
				mockMvc.perform(
						patch("/api/v1/location-comments")
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.characterEncoding("UTF-8")
								.content(
										objectMapper.writeValueAsString(request)
								)
				);

		// Then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value("???????????? ???????????? ?????????!!"))
				.andExpect(jsonPath("$.image").value("image.png"))
				.andDo(print());

	}

	@Test
	@DisplayName(" ????????? ????????? ????????? ??? ??????.")
	void deleteLocationComment() throws Exception {
		// Given
		LocationComment toBeDeleted = locationCommentRepository.save(
				LocationComment.builder()
						.member(member)
						.image("image.png")
						.locationId(location.getId())
						.content("?????? ?????????...")
						.partyMember(partyMember)
						.build()
		);

		// When
		ResultActions resultActions =
				mockMvc.perform(
						delete("/api/v1/location-comments/{id}", toBeDeleted.getId())
				);

		// Then
		resultActions
				.andExpect(status().isOk())
				.andDo(print());
	}
}
