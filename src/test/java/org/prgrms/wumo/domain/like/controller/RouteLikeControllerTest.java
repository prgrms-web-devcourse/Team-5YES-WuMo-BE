package org.prgrms.wumo.domain.like.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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
import org.prgrms.wumo.domain.party.repository.PartyRepository;
import org.prgrms.wumo.domain.route.model.Route;
import org.prgrms.wumo.domain.route.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("RouteLikeController 를 통해 ")
class RouteLikeControllerTest extends MysqlTestContainer {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PartyRepository partyRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private RouteRepository routeRepository;

	//given
	Member member;
	Party party;
	Location location;
	Route route;

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
		location = locationRepository.save(
				Location.builder()
						.category(Category.STUDY)
						.name("프로그래머스 대륭 서초 타워")
						.description("그렙!!")
						.latitude(12.123F)
						.longitude(12.123F)
						.address("서울특별시 서초구 강남대로327 2층 프로그래머스(서초동, 대륭서초타워)")
						.searchAddress("서울특별시")
						.visitDate(LocalDateTime.now())
						.image("http://grepp_image")
						.spending(10000)
						.expectedCost(10000)
						.partyId(party.getId())
						.build()
		);
		route = routeRepository.save(
				Route.builder()
						.id(1L)
						.locations(List.of(location))
						.party(party)
						.build()
		);
		route.updatePublicStatus(true);
		route = routeRepository.save(route);

		setAuthentication(member.getId());
	}

	@AfterEach
	void clean() {
		locationRepository.deleteById(location.getId());
		routeRepository.deleteById(route.getId());
		partyRepository.deleteById(party.getId());
		memberRepository.deleteById(member.getId());
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("루트에 좋아요를 누를 수 있다.")
	void validateInvitation() throws Exception {
		//when
		ResultActions resultActions = mockMvc.perform(post("/api/v1/routes/{routeId}/likes", route.getId()));

		//then
		resultActions
				.andExpect(status().isCreated())
				.andDo(print());
	}

	private void setAuthentication(Long memberId) {
		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(memberId, null, Collections.emptyList());
		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

}