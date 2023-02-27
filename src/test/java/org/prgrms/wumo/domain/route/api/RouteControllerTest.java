package org.prgrms.wumo.domain.route.api;

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
import org.prgrms.wumo.domain.location.model.Category;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.party.repository.PartyRepository;
import org.prgrms.wumo.domain.route.dto.request.RouteRegisterRequest;
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
@DisplayName("RouteController를 통해 ")
public class RouteControllerTest extends MysqlTestContainer {

	private long memberId;
	private long partyId;
	private long locationId;

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

	@BeforeEach
	void setup() {
		Member member = memberRepository.save(getMemberData());
		memberId = member.getId();

		Party party = partyRepository.save(getPartyData());
		partyId = party.getId();

		Location location = locationRepository.save(getLocationData());
		locationId = location.getId();

		partyMemberRepository.save(getPartyMemberData(member, party));

		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
			new UsernamePasswordAuthenticationToken(memberId, null, Collections.EMPTY_LIST);

		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	@AfterEach
	void tearDown() {
		partyMemberRepository.deleteAll();
		locationRepository.deleteAll();
		partyRepository.deleteAll();
		memberRepository.deleteAll();
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("후보지를 루트에 등록한다")
	void register_route() throws Exception {
		//given
		RouteRegisterRequest routeRegisterRequest
			= new RouteRegisterRequest(null, locationId, partyId);

		//when
		ResultActions resultActions
			= mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/routes")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(objectMapper.writeValueAsString(routeRegisterRequest)));

		//then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andDo(print());
	}

	private Member getMemberData() {
		return Member.builder()
			.email("taehee@gmail.com")
			.nickname("태희")
			.password("qwe12345")
			.build();
	}

	private Party getPartyData() {
		return Party.builder()
			.name("제주도 한달 살기")
			.coverImage("http://~~~.png")
			.startDate(LocalDateTime.now())
			.endDate(LocalDateTime.now().plusDays(1))
			.build();
	}

	private Location getLocationData() {
		return Location.builder()
			.name("오예스 찜닭")
			.latitude(12.3456F)
			.longitude(34.5678F)
			.address("제주시 서귀포시 서귀동")
			.image("http://~~~.png")
			.visitDate(LocalDateTime.now().plusDays(10))
			.category(Category.MEAL)
			.expectedCost(40000)
			.partyId(partyId)
			.build();
	}

	private PartyMember getPartyMemberData(Member member, Party party) {
		return PartyMember.builder()
			.member(member)
			.party(party)
			.role("총무")
			.isLeader(true)
			.build();
	}
}
