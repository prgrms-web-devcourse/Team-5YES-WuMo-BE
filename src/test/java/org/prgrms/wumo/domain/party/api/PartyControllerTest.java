package org.prgrms.wumo.domain.party.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.wumo.MysqlTestContainer;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.dto.request.PartyRegisterRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyUpdateRequest;
import org.prgrms.wumo.domain.party.dto.response.PartyRegisterResponse;
import org.prgrms.wumo.domain.party.service.PartyService;
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
@DisplayName("PartyController 를 통해 ")
class PartyControllerTest extends MysqlTestContainer {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PartyService partyService;

	@Autowired
	private MemberRepository memberRepository;

	private Member member;

	@BeforeEach
	void setup() {
		member = memberRepository.save(
				Member.builder()
						.email("ted-chang@gmail.com")
						.password("qwe12345")
						.nickname("테드창")
						.build()
		);

		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(member.getId(), null, Collections.emptyList());
		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	@AfterEach
	void clean() {
		memberRepository.deleteById(member.getId());
		member = null;
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("모임을 생성할 수 있다.")
	void registerParty() throws Exception {
		//given
		PartyRegisterRequest partyRegisterRequest = getPartyRegisterRequest();

		//when
		ResultActions resultActions = mockMvc.perform(post("/api/v1/parties")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(partyRegisterRequest)));

		//then
		resultActions
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andDo(print());
	}

	@Test
	@DisplayName("사용자의 모임 목록을 조회할 수 있다.")
	void getAllParty() throws Exception {
		//given
		PartyRegisterRequest partyRegisterRequest = getPartyRegisterRequest();
		PartyRegisterResponse partyRegisterResponse = partyService.registerParty(partyRegisterRequest);

		//when
		ResultActions resultActions = mockMvc.perform(get("/api/v1/parties/members/me")
				.param("partyType", "ALL")
				.param("cursorId", (String)null)
				.param("pageSize", "5"));

		//then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.party").isArray())
				.andExpect(jsonPath("$.party").isNotEmpty())
				.andExpect(jsonPath("$.party[0].id").value(partyRegisterResponse.id()))
				.andExpect(jsonPath("$.party[0].name").value(partyRegisterRequest.name()))
				.andExpect(jsonPath("$.party[0].coverImage").value(partyRegisterRequest.coverImage()))
				.andExpect(jsonPath("$.party[0].totalMembers").value(1L))
				.andExpect(jsonPath("$.party[0].members").isArray())
				.andExpect(jsonPath("$.party[0].members").isNotEmpty())
				.andExpect(jsonPath("$.lastId").isNotEmpty())
				.andDo(print());
	}

	@Test
	@DisplayName("모임의 상세 정보를 조회할 수 있다.")
	void getParty() throws Exception {
		//given
		PartyRegisterRequest partyRegisterRequest = getPartyRegisterRequest();
		PartyRegisterResponse partyRegisterResponse = partyService.registerParty(partyRegisterRequest);

		//when
		ResultActions resultActions = mockMvc.perform(get("/api/v1/parties/{partyId}", partyRegisterResponse.id()));

		//then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(partyRegisterResponse.id()))
				.andExpect(jsonPath("$.name").value(partyRegisterRequest.name()))
				.andExpect(jsonPath("$.startDate").isNotEmpty())  // 소수점 표기 기준이 달라 값이 있는지만 검증
				.andExpect(jsonPath("$.endDate").isNotEmpty())    // 위와 동일
				.andExpect(jsonPath("$.description").value(partyRegisterRequest.description()))
				.andExpect(jsonPath("$.coverImage").value(partyRegisterRequest.coverImage()))
				.andExpect(jsonPath("$.totalMembers").value(1L))
				.andExpect(jsonPath("$.members").isArray())
				.andExpect(jsonPath("$.members").isNotEmpty())
				.andDo(print());
	}

	@Test
	@DisplayName("특정 모임을 수정할 수 있다.")
	void updateParty() throws Exception {
		//given
		PartyRegisterRequest partyRegisterRequest = getPartyRegisterRequest();
		PartyRegisterResponse partyRegisterResponse = partyService.registerParty(partyRegisterRequest);
		PartyUpdateRequest partyUpdateRequest = new PartyUpdateRequest(
				"오예스 워크샵 (수정)",
				LocalDate.now().plusDays(1),
				LocalDate.now().plusDays(2),
				"팀 설립 기념 워크샵 (수정)",
				"https://~~~.jpeg"
		);

		//when
		ResultActions resultActions = mockMvc.perform(patch("/api/v1/parties/{partyId}", partyRegisterResponse.id())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(partyUpdateRequest)));

		//then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(partyRegisterResponse.id()))
				.andExpect(jsonPath("$.name").value(partyUpdateRequest.name()))
				.andExpect(jsonPath("$.startDate").isNotEmpty())
				.andExpect(jsonPath("$.endDate").isNotEmpty())
				.andExpect(jsonPath("$.description").value(partyUpdateRequest.description()))
				.andExpect(jsonPath("$.coverImage").value(partyUpdateRequest.coverImage()))
				.andExpect(jsonPath("$.totalMembers").value(1L))
				.andExpect(jsonPath("$.members").isArray())
				.andExpect(jsonPath("$.members").isNotEmpty())
				.andDo(print());
	}

	@Test
	@DisplayName("특정 모임을 삭제할 수 있다.")
	void deleteParty() throws Exception {
		//given
		PartyRegisterRequest partyRegisterRequest = getPartyRegisterRequest();
		PartyRegisterResponse partyRegisterResponse = partyService.registerParty(partyRegisterRequest);

		//when
		ResultActions resultActions = mockMvc.perform(delete("/api/v1/parties/{partyId}", partyRegisterResponse.id()));

		//then
		resultActions
				.andExpect(status().isOk())
				.andDo(print());
	}

	private PartyRegisterRequest getPartyRegisterRequest() {
		return new PartyRegisterRequest(
				"오예스 워크샵",
				LocalDate.now(),
				LocalDate.now().plusDays(1),
				"팀 설립 기념 워크샵",
				"https://~.jpeg",
				"총무"
		);
	}

}