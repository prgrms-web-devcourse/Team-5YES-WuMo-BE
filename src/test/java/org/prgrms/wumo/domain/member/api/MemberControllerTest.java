package org.prgrms.wumo.domain.member.api;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.wumo.MysqlTestContainer;
import org.prgrms.wumo.domain.member.dto.request.MemberEmailCheckRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberLoginRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberNicknameCheckRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberRegisterRequest;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("MemberController를 통해 ")
public class MemberControllerTest extends MysqlTestContainer {

	private long memberId;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MemberRepository memberRepository;

	@BeforeEach
	void setup() {
		Member member = memberRepository.save(getMemberData());
		memberId = member.getId();

		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
			new UsernamePasswordAuthenticationToken(memberId, null, Collections.EMPTY_LIST);

		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	@AfterEach
	void tearDown() {
		memberRepository.deleteAll();
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("회원가입을 한다")
	void register_member() throws Exception {
		//given
		MemberRegisterRequest memberRegisterRequest
			= new MemberRegisterRequest("5yes@gmail.com", "오예스", "qwe12345");

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
			= new MemberNicknameCheckRequest("오예스딸기");

		//when
		ResultActions resultActions
			= mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/members/check-nickname")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(objectMapper.writeValueAsString(memberNicknameCheckRequest)));

		//then
		resultActions
			.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("로그인을 한다")
	void login_member() throws Exception {
		//given
		MemberLoginRequest memberLoginRequest
			= new MemberLoginRequest("taehee@gmail.com", "qwe12345");

		//when
		ResultActions resultActions
			= mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/members/login")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(objectMapper.writeValueAsString(memberLoginRequest)));

		//then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.grantType").isNotEmpty())
			.andExpect(jsonPath("$.accessToken").isNotEmpty())
			.andExpect(jsonPath("$.refreshToken").isNotEmpty())
			.andDo(print());
	}

	@Test
	@DisplayName("내 정보를 조회한다")
	void get_member() throws Exception {
		//when
		ResultActions resultActions
			= mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/members/{memberId}", memberId));

		//then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(memberId))
			.andExpect(jsonPath("$.email").value("taehee@gmail.com"))
			.andExpect(jsonPath("$.nickname").value("태희"))
			.andExpect(jsonPath("$.profileImage").isEmpty())
			.andDo(print());
	}

	private Member getMemberData() {
		return Member.builder()
			.email("taehee@gmail.com")
			.nickname("태희")
			.password("qwe12345")
			.build();
	}
}
