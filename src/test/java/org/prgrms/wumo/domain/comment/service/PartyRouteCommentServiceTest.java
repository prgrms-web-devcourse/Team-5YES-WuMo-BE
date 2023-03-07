package org.prgrms.wumo.domain.comment.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.prgrms.wumo.global.mapper.CommentMapper.toPartyRouteCommentGetAllResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentGetAllRequest;
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentUpdateRequest;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentGetAllResponse;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentUpdateResponse;
import org.prgrms.wumo.domain.comment.model.PartyRouteComment;
import org.prgrms.wumo.domain.comment.repository.PartyRouteCommentRepository;
import org.prgrms.wumo.domain.location.model.Category;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.route.model.Route;
import org.springframework.security.access.AccessDeniedException;
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
	PartyMemberRepository partyMemberRepository;

	@Mock
	LocationRepository locationRepository;

	// GIVEN
	Member member;
	Party party;
	PartyMember partyMember;
	PartyRouteComment partyRouteComment;
	Route route;
	Location location;
	List<Location> locations = new ArrayList<>();

	@BeforeEach
	void beforeEach() {
		member = getMember();
		party = getParty();
		location = getLocation();
		locations.add(location);
		partyMember = getPartyMember();
		route = getRoute(locations);
		location.addRoute(route);
		partyRouteComment = getPartyRouteComment();

		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(member.getId(), null, Collections.EMPTY_LIST);

		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	@AfterEach
	void afterEach() {
		locations.clear();
		SecurityContextHolder.clearContext();
	}

	@Nested
	@DisplayName("registerPartyRouteComment를 통해 ")
	class registerPartyRouteComment {
		@Test
		@DisplayName("모임 내 일정에 댓글을 등록한다")
		void success() {
			// Given
			PartyRouteCommentRegisterRequest request = new PartyRouteCommentRegisterRequest(
					"모임 내 댓글", "image.png", party.getId(), location.getId()
			);

			given(locationRepository.findById(any(Long.class))).willReturn(Optional.of(location));
			given(partyMemberRepository.findByPartyIdAndMemberId(any(Long.class), any(Long.class))).willReturn(
					Optional.of(partyMember));
			given(partyRouteCommentRepository.save(any(PartyRouteComment.class))).willReturn(partyRouteComment);

			// When
			PartyRouteCommentRegisterResponse response =
					partyRouteCommentService.registerPartyRouteComment(request);

			// Then
			assertThat(response.id()).isEqualTo(1L);
		}
	}

	@Nested
	@DisplayName("getAllPartyRouteComment를 통해 ")
	class getAllPartyRouteComment {

		@Test
		@DisplayName("모임 내 댓글들 중 특정 후보지의 댓글 목록을 조회할 수 있다.")
		void success() {
			// Given
			PartyRouteComment partyRouteComment2 = PartyRouteComment.builder()
					.id(2L)
					.routeId(route.getId())
					.partyMember(partyMember)
					.image("image.png")
					.content("댓글 댓글 댓글")
					.locationId(2L)
					.member(member)
					.build();

			PartyRouteComment partyRouteComment3 = PartyRouteComment.builder()
					.id(3L)
					.routeId(route.getId())
					.partyMember(partyMember)
					.image("image.png")
					.content("댓글 댓글 댓글")
					.locationId(2L)
					.member(member)
					.build();

			List<PartyRouteComment> partyRouteComments = List.of(partyRouteComment2, partyRouteComment3);
			PartyRouteCommentGetAllRequest request = new PartyRouteCommentGetAllRequest(null, 2, 2L);
			PartyRouteCommentGetAllResponse expected = toPartyRouteCommentGetAllResponse(partyRouteComments, 3L);

			// When
			given(partyRouteCommentRepository.findAllByLocationId(null, 2, 2L))
					.willReturn(partyRouteComments);

			// Then
			PartyRouteCommentGetAllResponse partyRouteCommentGetAllResponse =
					partyRouteCommentService.getAllPartyRouteComment(request);

			assertThat(partyRouteCommentGetAllResponse.partyRouteComments().size()).isEqualTo(2);
			assertThat(partyRouteCommentGetAllResponse).usingRecursiveComparison().isEqualTo(expected);
		}
	}

	@Nested
	@DisplayName("PartyRouteCommentUpdate를 통해 ")
	class updatePartyRouteComment {
		@Test
		@DisplayName("모임 내 루트 댓글을 수정할 수 있다.")
		void success() {
			// Given
			given(partyMemberRepository.existsById(any(Long.class))).willReturn(true);
			given(partyRouteCommentRepository.findById(any(Long.class))).willReturn(Optional.of(partyRouteComment));
			given(partyRouteCommentRepository.save(any(PartyRouteComment.class))).willReturn(partyRouteComment);

			PartyRouteCommentUpdateRequest partyRouteCommentUpdateRequest =
					new PartyRouteCommentUpdateRequest(partyRouteComment.getRouteId(), "여기 좋아요!", "image");
			PartyRouteCommentUpdateResponse expected =
					new PartyRouteCommentUpdateResponse(partyRouteComment.getRouteId(), "여기 좋아요!", "image");

			// When
			PartyRouteCommentUpdateResponse partyRouteCommentUpdateResponse = partyRouteCommentService.updatePartyRouteComment(
					partyRouteCommentUpdateRequest);

			// Then
			assertThat(partyRouteCommentUpdateResponse).usingRecursiveComparison().isEqualTo(expected);
		}

		@Test
		@DisplayName("자신의 댓글이 아니면 수정할 수 없다.")
		void failWithAccessDenied() {
			// Given
			given(partyMemberRepository.existsById(any(Long.class))).willReturn(false);
			given(partyRouteCommentRepository.findById(any(Long.class))).willReturn(Optional.of(partyRouteComment));

			PartyRouteCommentUpdateRequest partyRouteCommentUpdateRequest =
					new PartyRouteCommentUpdateRequest(partyRouteComment.getRouteId(), "여기 좋아요!", "image");

			// When // Then
			assertThatThrownBy(
					() -> partyRouteCommentService.updatePartyRouteComment(partyRouteCommentUpdateRequest)
			).isInstanceOf(AccessDeniedException.class);

		}
	}

	@Nested
	@DisplayName("deletePartyRouteComment 를 통해 ")
	class deletePartyRouteComment {
		@Test
		@DisplayName("모임 내 댓글을 삭제할 수 있다.")
		void success() {
			// Given
			PartyRouteComment tmp = PartyRouteComment.builder()
					.id(450L)
					.image("image.png")
					.content("삭제될 댓글....")
					.locationId(location.getId())
					.routeId(route.getId())
					.partyMember(partyMember)
					.member(member)
					.build();

			given(partyMemberRepository.existsById(any(Long.class))).willReturn(true);
			given(partyRouteCommentRepository.findById(any(Long.class))).willReturn(Optional.of(tmp));

			// When
			partyRouteCommentService.deletePartyRouteComment(450L);

			// Then
			then(partyRouteCommentRepository).should().findById(450L);
		}

		@Test
		@DisplayName("댓글을 작성하지 않은 사람은 댓글을 삭제할 수 없다.")
		void failedByOtherMember() {
			// Given
			PartyRouteComment tmp = PartyRouteComment.builder()
					.id(450L)
					.image("image.png")
					.content("삭제될 댓글....")
					.locationId(location.getId())
					.routeId(route.getId())
					.partyMember(partyMember)
					.member(member)
					.build();

			given(partyMemberRepository.existsById(any(Long.class))).willReturn(false);
			given(partyRouteCommentRepository.findById(any(Long.class))).willReturn(Optional.of(tmp));

			// When // Then
			assertThatThrownBy(
					() -> partyRouteCommentService.deletePartyRouteComment(450L)
			).isInstanceOf(AccessDeniedException.class);
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
				.description("오예스팀 모임")
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
				.routeId(route.getId())
				.partyMember(partyMember)
				.image("image.png")
				.content("모임 내 댓글")
				.locationId(location.getId())
				.member(member)
				.build();
	}

	private Route getRoute(List<Location> locations) {
		return Route.builder()
				.id(1L)
				.party(party)
				.locations(locations)
				.build();
	}

	private Location getLocation() {
		return Location.builder()
				.id(1L)
				.partyId(party.getId())
				.expectedCost(50000)
				.spending(45000)
				.searchAddress("고양시")
				.address("경기도 고양시 일산서구")
				.name("내 집")
				.description("언제 와도 좋은 서윗한 내 집")
				.longitude(12.34F)
				.latitude(67.89F)
				.category(Category.DRINK)
				.visitDate(LocalDateTime.now())
				.route(route)
				.build();
	}
}

