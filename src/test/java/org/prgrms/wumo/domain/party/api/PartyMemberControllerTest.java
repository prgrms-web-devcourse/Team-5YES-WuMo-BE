package org.prgrms.wumo.domain.party.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.party.repository.PartyRepository;
import org.prgrms.wumo.domain.party.service.PartyMemberService;
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
	private PartyMemberService partyMemberService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PartyRepository partyRepository;

	@Autowired
	private PartyMemberRepository partyMemberRepository;

	private Member leader;

	private Member participant;

	private Party party;

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
		partyMemberRepository.save(
				PartyMember.builder()
						.member(leader)
						.party(party)
						.role("총무")
						.isLeader(true)
						.build()
		);

		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(participant.getId(), null, Collections.emptyList());
		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	@AfterEach
	void clean() {
		partyMemberRepository.deleteAll();
		partyRepository.deleteById(party.getId());
		memberRepository.deleteById(leader.getId());
		memberRepository.deleteById(participant.getId());
		leader = null;
		participant = null;
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("특정 사용자가 모임에 참여할 수 있다.")
	void registerParty() throws Exception {
		//given
		PartyMemberRegisterRequest partyMemberRegisterRequest = getPartyMemberRegisterRequest();

		//when
		ResultActions resultActions = mockMvc.perform(post("/api/v1/parties/" + party.getId() + "/members")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(partyMemberRegisterRequest)));

		//then
		resultActions
				.andExpect(status().isCreated())
				.andDo(print());
	}

	private PartyMemberRegisterRequest getPartyMemberRegisterRequest() {
		return new PartyMemberRegisterRequest("운전기사");
	}

}