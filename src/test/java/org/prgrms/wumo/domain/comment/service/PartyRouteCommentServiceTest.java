package org.prgrms.wumo.domain.comment.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.model.PartyRouteComment;
import org.prgrms.wumo.domain.comment.repository.PartyRouteCommentRepository;
import org.prgrms.wumo.domain.location.model.Category;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.route.model.Route;
import org.prgrms.wumo.domain.route.repository.RouteRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@DisplayName("PartyRouteCommentService 에서")
@ExtendWith(MockitoExtension.class)
public class PartyRouteCommentServiceTest {

	@InjectMocks
	PartyRouteCommentService partyRouteCommentService;

	@Mock
	PartyRouteCommentRepository partyRouteCommentRepository;

	@Mock
	MemberRepository memberRepository;

	@Mock
	PartyMemberRepository partyMemberRepository;

	@Mock
	LocationRepository locationRepository;

	@Mock
	RouteRepository routeRepository;

	// GIVEN
	Member member;
	Party party;
	PartyMember partyMember;
	PartyRouteComment partyRouteComment;
	Route route;
	Location location;

	@BeforeEach
	void beforeEach() {
		member = getMember();
		party = getParty();
		partyMember = getPartyMember();
		partyRouteComment = getPartyRouteComment();
		route = getRoute();
		location = getLocation();

		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(1L, null, Collections.EMPTY_LIST);

		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	@AfterEach
	void afterEach() {
		SecurityContextHolder.clearContext();
	}

	@Nested
	@DisplayName("registerPartyRouteComment를 통해 ")
	class registerPartyRouteComment{
		@Test
		@DisplayName("모임 내 일정에 댓글을 등록한다")
		void success(){
			// Given
			PartyRouteCommentRegisterRequest request = new PartyRouteCommentRegisterRequest(
					1L, "모임 내 댓글", "image.png", 1L, 1L
			);

			given(memberRepository.findById(any(Long.class))).willReturn(Optional.of(member));
			given(partyMemberRepository.findByPartyIdAndMemberId(any(Long.class), any(Long.class))).willReturn(Optional.of(partyMember));
			given(routeRepository.findById(any(Long.class))).willReturn(Optional.of(route));
			given(locationRepository.existsById(any(Long.class))).willReturn(true);
			given(partyRouteCommentRepository.save(any(PartyRouteComment.class))).willReturn(partyRouteComment);

			// When
			PartyRouteCommentRegisterResponse response =
					partyRouteCommentService.registerPartyRouteComment(request);

			// Then
			assertThat(response.id()).isEqualTo(1L);
		}
	}

	private Member getMember() {
		return Member.builder()
				.id(1L)
				.password("qwe12345")
				.email("member@email.com")
				.nickname("nickname")
				.build();
	}

	private Party getParty() {
		return Party.builder()
				.id(1L)
				.password("1234").description("오예스팀 모임")
				.coverImage("party_cover_image.png")
				.name("오예스")
				.endDate(LocalDateTime.now().plusDays(5))
				.startDate(LocalDateTime.now().plusDays(2))
				.build();
	}

	private PartyMember getPartyMember() {
		return PartyMember.builder()
				.id(1L)
				.member(member)
				.party(party)
				.role("총무")
				.isLeader(true)
				.build();
	}

	private PartyRouteComment getPartyRouteComment() {
		return PartyRouteComment.builder()
				.id(1L)
				.routeId(1L)
				.partyMember(partyMember)
				.image("image.png")
				.content("모임 내 댓글")
				.isEdited(false)
				.locationId(1L)
				.member(member)
				.build();
	}

	private Route getRoute(){
		return Route.builder()
				.id(1L)
				.party(party)
				.locations(List.of())
				.build();
	}

	private Location getLocation(){
		return Location.builder()
				.id(1L)
				.partyId(party.getId())
				.expectedCost(50000)
				.spending(45000)
				.address("경기도 고양시 일산서구")
				.name("내 집")
				.description("언제 와도 좋은 서윗한 내 집")
				.longitude(12.34F)
				.latitude(67.89F)
				.category(Category.DRINK)
				.visitDate(LocalDateTime.now())
				.build();
	}
}
