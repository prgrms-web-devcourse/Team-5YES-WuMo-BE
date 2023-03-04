package org.prgrms.wumo.domain.party.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.wumo.MysqlTestContainer;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.dto.request.InvitationRegisterRequest;
import org.prgrms.wumo.domain.party.model.Invitation;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.InvitationRepository;
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
@DisplayName("InvitationController 를 통해 ")
class InvitationControllerTest extends MysqlTestContainer {

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

	@Autowired
	private InvitationRepository invitationRepository;

	private Member member;

	private Party party;

	private PartyMember partyMember;

	@BeforeEach
	void setup() {
		member = memberRepository.save(
				Member.builder()
						.email("ted-chang@gmail.com")
						.password("qwe12345")
						.nickname("테드창")
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
		partyMember = partyMemberRepository.save(
				PartyMember.builder()
						.member(member)
						.party(party)
						.role("총무")
						.isLeader(true)
						.build()
		);

		setAuthentication(member.getId());
	}

	@AfterEach
	void clean() {
		partyMemberRepository.deleteById(partyMember.getId());
		partyRepository.deleteById(party.getId());
		memberRepository.deleteById(member.getId());
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("모임 구성원이 초대코드를 생성할 수 있다.")
	void registerInvitation() throws Exception {
		//given
		InvitationRegisterRequest invitationRegisterRequest = getInvitationRegisterRequest();

		//when
		ResultActions resultActions = mockMvc.perform(post("/api/v1/parties/{partyId}/invitations", party.getId())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(invitationRegisterRequest)));

		//then
		resultActions
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.code").isNotEmpty())
				.andDo(print());
	}

	@Test
	@DisplayName("초대코드의 유효성 검증을 할 수 있다.")
	void validateInvitation() throws Exception {
		//given
		Invitation invitation = invitationRepository.save(
				Invitation.builder()
						.party(party)
						.expiredDate(LocalDateTime.now().plusDays(7))
						.build()
		);

		//when
		ResultActions resultActions = mockMvc.perform(get("/api/v1/parties/invitations/{code}", invitation.getCode()));

		//then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.partyId").value(party.getId()))
				.andDo(print());
	}

	private void setAuthentication(Long memberId) {
		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(memberId, null, Collections.emptyList());
		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	private InvitationRegisterRequest getInvitationRegisterRequest() {
		return new InvitationRegisterRequest(LocalDate.now().plusDays(7));
	}

}