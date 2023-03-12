package org.prgrms.wumo.domain.comment.api;

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
import org.prgrms.wumo.domain.comment.dto.request.ReplyCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.model.LocationComment;
import org.prgrms.wumo.domain.comment.model.ReplyComment;
import org.prgrms.wumo.domain.comment.repository.LocationCommentRepository;
import org.prgrms.wumo.domain.comment.repository.ReplyCommentRepository;
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
@DisplayName("ReplyCommentController를 통해 ")
public class ReplyCommentControllerTest extends MysqlTestContainer {
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
	@Autowired
	private ReplyCommentRepository replyCommentRepository;

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

		location = locationRepository.save(
				Location.builder()
						.category(Category.COFFEE)
						.memberId(member.getId())
						.visitDate(LocalDateTime.now().plusDays(4))
						.description("아인슈페너가 맛있는 곳!")
						.name("cafe")
						.address("경기도 고양시 일산서구")
						.searchAddress("고양시")
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
						.content("댓글 댓글")
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
	@DisplayName("후보지 댓글에 대댓글을 작성할 수 있다.")
	void registerReplyOnLocationComment() throws Exception {
		// Given
		ReplyCommentRegisterRequest replyCommentRegisterRequest =
				new ReplyCommentRegisterRequest(locationComment.getId(), "대댓글 쓰자!!!");

		// When
		ResultActions resultActions =
				mockMvc.perform(
						post("/api/v1/reply-comments/location-comments")
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.characterEncoding("UTF-8")
								.content(
										objectMapper.writeValueAsString(replyCommentRegisterRequest)
								)
				);

		// Then
		resultActions
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andDo(print());
	}

	@Test
	@DisplayName("후보지 댓글에 대댓글을 작성할 수 있다.")
	void getAllReplyCommentTest() throws Exception {
		// Given
		ReplyComment replyComment1 = ReplyComment.builder()
				.commentId(locationComment.getId())
				.member(member)
				.content("대댓글 1호!")
				.build();
		ReplyComment replyComment2 = ReplyComment.builder()
				.commentId(locationComment.getId())
				.member(member)
				.content("대댓글 2호!")
				.build();

		replyCommentRepository.saveAll(List.of(replyComment1, replyComment2));

		// When
		ResultActions resultActions =
				mockMvc.perform(
						get("/api/v1/reply-comments")
								.param("cursorId", (String)null)
								.param("pageSize", "2")
								.param("commentId", String.valueOf(locationComment.getId()))
				);

		// Then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.replyComments").isNotEmpty())
				.andExpect(jsonPath("$.replyComments").isArray())
				.andExpect(jsonPath("$.lastId").isNotEmpty())
				.andDo(print());
	}
}
