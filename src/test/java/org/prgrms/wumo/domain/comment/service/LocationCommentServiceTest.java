package org.prgrms.wumo.domain.comment.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.prgrms.wumo.global.mapper.CommentMapper.toLocationCommentGetAllResponse;

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
import org.prgrms.wumo.domain.comment.dto.request.LocationCommentGetAllRequest;
import org.prgrms.wumo.domain.comment.dto.request.LocationCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.request.LocationCommentUpdateRequest;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentGetAllResponse;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentUpdateResponse;
import org.prgrms.wumo.domain.comment.model.LocationComment;
import org.prgrms.wumo.domain.comment.repository.LocationCommentRepository;
import org.prgrms.wumo.domain.location.model.Category;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

@DisplayName("LocationService 에서")
@ExtendWith(MockitoExtension.class)
public class LocationCommentServiceTest {
	@InjectMocks
	LocationCommentService locationCommentService;

	@Mock
	LocationCommentRepository locationCommentRepository;

	@Mock
	MemberRepository memberRepository;

	@Mock
	PartyMemberRepository partyMemberRepository;

	// GIVEN
	Member member;
	LocationComment locationComment;
	Party party;
	PartyMember partyMember;
	Location location;

	@BeforeEach
	void beforeEach() {
		member = getMember();
		party = getParty();
		partyMember = getPartyMember();
		location = getLocation();
		locationComment = getLocationComment();

		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(member.getId(), null, Collections.EMPTY_LIST);

		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	@AfterEach
	void afterEach() {
		SecurityContextHolder.clearContext();
	}

	@Nested
	@DisplayName("registerComment를 통해 ")
	class RegisterLocationComment {

		@Test
		@WithMockUser
		@DisplayName("후보지 댓글을 등록한다.")
		void registerLocationComment() {
			// Given
			LocationCommentRegisterRequest locationCommentRegisterRequest =
					new LocationCommentRegisterRequest(locationComment.getContent(), locationComment.getImage(),
							locationComment.getLocationId(), locationComment.getPartyMember().getId());

			given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
			given(partyMemberRepository.findById(any(Long.class))).willReturn(Optional.of(partyMember));
			given(locationCommentRepository.save(any(LocationComment.class))).willReturn(locationComment);

			// When
			LocationCommentRegisterResponse locationCommentRegisterResponse =
					locationCommentService.registerLocationComment(locationCommentRegisterRequest);

			// Then
			assertThat(locationCommentRegisterResponse.id()).isEqualTo(1L);

		}
	}

	@Nested
	@DisplayName("getAllComments를 통해")
	class getAllLocationComment {

		@Test
		@DisplayName("후보지 댓글들을 조회한다.")
		void getAllLocationComments() {
			// Given
			LocationComment locationComment2 = LocationComment.builder()
					.id(2L)
					.image("image.png")
					.content("첫 번째 댓글")
					.locationId(2L)
					.partyMember(partyMember)
					.member(member)
					.build();

			LocationComment locationComment3 = LocationComment.builder()
					.id(3L)
					.image("image.png")
					.content("두 번째 댓글")
					.locationId(2L)
					.partyMember(partyMember)
					.member(member)
					.build();

			List<LocationComment> locationComments = List.of(locationComment2, locationComment3);

			LocationCommentGetAllRequest locationCommentGetAllRequest =
					new LocationCommentGetAllRequest(null, 2, 2L);

			LocationCommentGetAllResponse expected = toLocationCommentGetAllResponse(locationComments, 3L);

			given(locationCommentRepository.findAllByLocationId(2L, null, 2))
					.willReturn(locationComments);

			// When
			LocationCommentGetAllResponse locationCommentGetAllResponse =
					locationCommentService.getAllLocationComments(locationCommentGetAllRequest);

			// Then
			assertThat(locationCommentGetAllResponse.locationComments().size()).isEqualTo(2);
			assertThat(locationCommentGetAllResponse).usingRecursiveComparison().isEqualTo(expected);
		}

	}

	@Nested
	@DisplayName("LocationCommentUpdate를 통해 ")
	class locationCommentUpdate {
		@Test
		@DisplayName("후보지 댓글을 수정한다.")
		void success() {
			// Given
			given(partyMemberRepository.existsById(any(Long.class))).willReturn(true);
			given(locationCommentRepository.findById(any(Long.class))).willReturn(Optional.of(locationComment));
			given(locationCommentRepository.save(any(LocationComment.class))).willReturn(locationComment);

			LocationCommentUpdateRequest request =
					new LocationCommentUpdateRequest(locationComment.getId(), "다음에는 반얀트리 가야지!!", "image.png");
			LocationCommentUpdateResponse expected =
					new LocationCommentUpdateResponse(locationComment.getId(), "다음에는 반얀트리 가야지!!", "image.png");
			// When
			LocationCommentUpdateResponse response = locationCommentService.updateLocationComment(request);

			// Then
			assertThat(response).usingRecursiveComparison().isEqualTo(expected);

		}

		@Nested
		@DisplayName("LocationCommentDelete를 통해")
		class locationCommentDelete {
			@Test
			@DisplayName("후보지 댓글을 삭제한다.")
			void success() {
				// Given
				LocationComment locationCommentTmp = LocationComment.builder()
						.id(450L)
						.image("image.png")
						.content("삭제될 댓글....")
						.locationId(location.getId())
						.partyMember(partyMember)
						.member(member)
						.build();

				given(partyMemberRepository.existsById(any(Long.class))).willReturn(true);
				given(locationCommentRepository.findById(any(Long.class))).willReturn(Optional.of(locationCommentTmp));

				// When
				locationCommentService.deleteLocationComment(450L);

				// Then
				then(locationCommentRepository).should().findById(450L);

			}

			@Test
			@DisplayName("댓글을 작성하지 않은 사람은 댓글을 삭제할 수 없다.")
			void failedByOtherMember() {
				// Given
				LocationComment locationCommentTmp = LocationComment.builder()
						.id(450L)
						.image("image.png")
						.content("삭제될 댓글....")
						.locationId(location.getId())
						.partyMember(partyMember)
						.member(member)
						.build();

				given(partyMemberRepository.existsById(any(Long.class))).willReturn(false);
				given(locationCommentRepository.findById(any(Long.class))).willReturn(Optional.of(locationCommentTmp));

				// When
				assertThatThrownBy(
						() -> locationCommentService.deleteLocationComment(450L)
				).isInstanceOf(AccessDeniedException.class);

				// Then

			}
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

	private LocationComment getLocationComment() {
		return LocationComment.builder()
				.id(1L)
				.image("image.png")
				.content("댓글 댓글")
				.locationId(location.getId())
				.partyMember(partyMember)
				.member(member)
				.build();
	}

	private Location getLocation() {
		return Location.builder()
				.id(1L)
				.image("httpL//.png")
				.category(Category.MEAL)
				.description("딸기 뷔페!! 드디어 예약 성공!! Must Be StrawBerry!!!")
				.address("서울특별시 중구 을지로 30 페닌슐라 라운지 & 바")
				.searchAddress("서울특별시")
				.name("롯데호텔")
				.latitude(123.45F)
				.longitude(34.45F)
				.partyId(party.getId())
				.visitDate(LocalDateTime.now())
				.expectedCost(170000)
				.build();
	}
}
