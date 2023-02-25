package org.prgrms.wumo.domain.party.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.wumo.MysqlTestContainer;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.dto.request.PartyRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
	private MemberRepository memberRepository;

	private Member member;

	@BeforeEach
	void setup() {
		member = memberRepository.save(
				Member.builder()
						.email("ted-change@gmail.com")
						.password("qwe12345")
						.nickname("테드창")
						.build()
		);
	}

	@AfterEach
	void clean() {
		memberRepository.deleteAll();
		member = null;
	}

	@Test
	@DisplayName("모임을 생성할 수 있다.")
	void registerParty() throws Exception {
		//given
		PartyRegisterRequest partyRegisterRequest = new PartyRegisterRequest(
				"오예스 워크샵",
				LocalDateTime.now(),
				LocalDateTime.now().plusDays(1),
				"팀 설립 기념 워크샵",
				"https://~.jpeg",
				"1234",
				member.getId(),
				"총무"
		);

		//when
		ResultActions resultActions = mockMvc.perform(post("/api/v1/party")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(partyRegisterRequest)));

		//then
		resultActions
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andDo(print());
	}

}