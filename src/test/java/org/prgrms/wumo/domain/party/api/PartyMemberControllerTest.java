package org.prgrms.wumo.domain.party.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.wumo.MysqlTestContainer;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.dto.request.PartyMemberRegisterRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyMemberUpdateRequest;
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
@DisplayName("PartyMemberController ??? ?????? ")
class PartyMemberControllerTest extends MysqlTestContainer {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PartyRepository partyRepository;

	@Autowired
	private PartyMemberRepository partyMemberRepository;

	private Member leader;

	private Member participant;

	private Party party;

	private PartyMember partyLeader;

	@BeforeEach
	void setup() {
		leader = memberRepository.save(
				Member.builder()
						.email("ted-chang@gmail.com")
						.password("qwe12345")
						.nickname("?????????")
						.build()
		);
		participant = memberRepository.save(
				Member.builder()
						.email("ted-chang@naver.com")
						.password("qwe12345")
						.nickname("????????????")
						.build()
		);
		party = partyRepository.save(
				Party.builder()
						.name("????????? ?????????")
						.startDate(LocalDateTime.now())
						.endDate(LocalDateTime.now().plusDays(1))
						.description("??? ?????? ?????? ?????????")
						.coverImage("https://~.jpeg")
						.build()
		);
		partyLeader = partyMemberRepository.save(
				PartyMember.builder()
						.member(leader)
						.party(party)
						.role("??????")
						.isLeader(true)
						.build()
		);

		setAuthentication(participant.getId());
	}

	@AfterEach
	void clean() {
		partyMemberRepository.deleteById(partyLeader.getId());
		partyRepository.deleteById(party.getId());
		memberRepository.deleteById(leader.getId());
		memberRepository.deleteById(participant.getId());
		leader = null;
		participant = null;
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("?????? ???????????? ????????? ????????? ??? ??????.")
	void registerPartyMember() throws Exception {
		//given
		PartyMemberRegisterRequest partyMemberRegisterRequest = getPartyMemberRegisterRequest();

		//when
		ResultActions resultActions = mockMvc.perform(post("/api/v1/parties/{partyId}/members", party.getId())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(partyMemberRegisterRequest)));

		//then
		resultActions
				.andExpect(status().isCreated())
				.andDo(print());
	}

	@Test
	@DisplayName("?????? ????????? ????????? ????????? ????????? ??? ??????.")
	void getAllPartyMembers() throws Exception {
		//given
		setAuthentication(leader.getId());

		//when
		ResultActions resultActions = mockMvc.perform(get("/api/v1/parties/{partyId}/members", party.getId())
						.param("cursorId", (String)null)
						.param("pageSize", "5"));

		//then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.members").isArray())
				.andExpect(jsonPath("$.members").isNotEmpty())
				.andExpect(jsonPath("$.members[0].memberId").value(leader.getId()))
				.andExpect(jsonPath("$.members[0].nickname").value(leader.getNickname()))
				.andExpect(jsonPath("$.members[0].role").value(partyLeader.getRole()))
				.andExpect(jsonPath("$.members[0].profileImage").value(leader.getProfileImage()))
				.andExpect(jsonPath("$.members[0].isLeader").value(partyLeader.isLeader()))
				.andExpect(jsonPath("$.lastId").isNotEmpty())
				.andDo(print());
	}

	@Test
	@DisplayName("?????? ??? ????????? ????????? ????????? ??? ??????.")
	void getPartyMember() throws Exception {
		//given
		setAuthentication(leader.getId());

		//when
		ResultActions resultActions = mockMvc.perform(get("/api/v1/parties/{partyId}/members/me", party.getId()));

		//then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.memberId").value(leader.getId()))
				.andExpect(jsonPath("$.nickname").value(leader.getNickname()))
				.andExpect(jsonPath("$.role").value(partyLeader.getRole()))
				.andExpect(jsonPath("$.profileImage").value(leader.getProfileImage()))
				.andExpect(jsonPath("$.isLeader").value(partyLeader.isLeader()))
				.andDo(print());
	}

	@Test
	@DisplayName("?????? ????????? ????????? ????????? ????????? ??? ??????.")
	void updatePartyMember() throws Exception {
		//given
		setAuthentication(leader.getId());
		PartyMemberUpdateRequest partyMemberUpdateRequest = getPartyMemberUpdateRequest();

		//when
		ResultActions resultActions = mockMvc.perform(patch("/api/v1/parties/{partyId}/members", party.getId())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(partyMemberUpdateRequest)));

		//then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.memberId").value(leader.getId()))
				.andExpect(jsonPath("$.nickname").value(leader.getNickname()))
				.andExpect(jsonPath("$.role").value(getPartyMemberUpdateRequest().role()))
				.andExpect(jsonPath("$.profileImage").value(leader.getProfileImage()))
				.andDo(print());
	}

	@Test
	@DisplayName("?????? ???????????? ???????????? ????????? ??? ??????.")
	void deletePartyMemberWithLeader() throws Exception {
		//given
		partyMemberRepository.save(getPartyParticipant());
		setAuthentication(leader.getId());

		//when
		ResultActions resultActions = mockMvc.perform(delete("/api/v1/parties/{partyId}/members/{memberId}", party.getId(), participant.getId()));

		//then
		resultActions
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	@DisplayName("???????????? ????????? ??? ??????.")
	void deletePartyMember() throws Exception {
		//given
		partyMemberRepository.save(getPartyParticipant());

		//when
		ResultActions resultActions = mockMvc.perform(delete("/api/v1/parties/{partyId}/members", party.getId()));

		//then
		resultActions
				.andExpect(status().isOk())
				.andDo(print());
	}

	private void setAuthentication(Long memberId) {
		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(memberId, null, Collections.emptyList());
		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	private PartyMemberRegisterRequest getPartyMemberRegisterRequest() {
		return new PartyMemberRegisterRequest("????????????");
	}

	private PartyMemberUpdateRequest getPartyMemberUpdateRequest() {
		return new PartyMemberUpdateRequest("????????????");
	}

	private PartyMember getPartyParticipant() {
		return PartyMember.builder()
				.member(participant)
				.party(party)
				.role("?????????")
				.isLeader(false)
				.build();
	}

}