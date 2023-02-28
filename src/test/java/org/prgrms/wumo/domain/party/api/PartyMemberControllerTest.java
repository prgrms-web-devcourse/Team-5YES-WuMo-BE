package org.prgrms.wumo.domain.party.api;

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
@DisplayName("PartyMemberController 를 통해 ")
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
						.nickname("테드창")
						.build()
		);
		participant = memberRepository.save(
				Member.builder()
						.email("ted-chang@naver.com")
						.password("qwe12345")
						.nickname("테드창규")
						.build()
		);
		party = partyRepository.save(
				Party.builder()
						.name("오예스 워크샵")
						.startDate(LocalDateTime.now())
						.endDate(LocalDateTime.now().plusDays(1))
						.description("팀 설립 기념 워크샵")
						.coverImage("https://~.jpeg")
						.password("1234")
						.build()
		);
		partyLeader = partyMemberRepository.save(
				PartyMember.builder()
						.member(leader)
						.party(party)
						.role("총무")
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
	@DisplayName("특정 사용자가 모임에 참여할 수 있다.")
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
	@DisplayName("특정 모임의 구성원을 조회할 수 있다.")
	void getAllPartyMembers() throws Exception {
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
				.andExpect(jsonPath("$.lastId").isNotEmpty())
				.andDo(print());
	}

	@Test
	@DisplayName("모임 내에서 자신의 정보를 수정할 수 있다.")
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

	private void setAuthentication(Long memberId) {
		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(memberId, null, Collections.emptyList());
		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	private PartyMemberRegisterRequest getPartyMemberRegisterRequest() {
		return new PartyMemberRegisterRequest("운전기사");
	}
	private PartyMemberUpdateRequest getPartyMemberUpdateRequest() {
		return new PartyMemberUpdateRequest("드라이버");
	}

}