package org.prgrms.wumo.domain.member.api;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.wumo.MysqlTestContainer;
import org.prgrms.wumo.domain.member.dto.request.MemberEmailCheckRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberNicknameCheckRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("MemberController를 통해 ")
public class MemberControllerTest extends MysqlTestContainer {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("회원가입을 한다")
	void register_member() throws Exception {
		//given
		MemberRegisterRequest memberRegisterRequest
			= new MemberRegisterRequest("5yes@gmail.com", "오예스오리지널", "qwe12345");

		//when
		ResultActions resultActions
			= mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/members/signup")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(objectMapper.writeValueAsString(memberRegisterRequest)));

		//then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andDo(print());
	}

	@Test
	@DisplayName("회원 이메일의 중복체크를 한다")
	void check_email_not_duplicate() throws Exception {
		//given
		MemberEmailCheckRequest memberEmailCheckRequest
			= new MemberEmailCheckRequest("5yes-check@gmail.com");

		//when
		ResultActions resultActions
			= mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/members/check-email")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(objectMapper.writeValueAsString(memberEmailCheckRequest)));

		//then
		resultActions
			.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("회원 닉네임의 중복체크를 한다")
	void check_nickname_not_duplicate() throws Exception {
		//given
		MemberNicknameCheckRequest memberNicknameCheckRequest
			= new MemberNicknameCheckRequest("오예스바나나");

		//when
		ResultActions resultActions
			= mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/members/check-nickname")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(objectMapper.writeValueAsString(memberNicknameCheckRequest)));

		//then
		resultActions
			.andExpect(status().isNoContent());
	}
}
