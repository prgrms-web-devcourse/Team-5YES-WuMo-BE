package org.prgrms.wumo.domain.comment.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.prgrms.wumo.domain.comment.mapper.CommentMapper.toLocationCommentGetAllResponse;

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
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

@DisplayName("LocationService ??????")
@ExtendWith(MockitoExtension.class)
public class LocationCommentServiceTest {
	@InjectMocks
	LocationCommentService locationCommentService;

	@Mock
	LocationCommentRepository locationCommentRepository;

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
	@DisplayName("registerComment??? ?????? ")
	class RegisterLocationComment {

		@Test
		@WithMockUser
		@DisplayName("????????? ????????? ????????????.")
		void registerLocationComment() {
			// Given
			LocationCommentRegisterRequest locationCommentRegisterRequest =
					new LocationCommentRegisterRequest(locationComment.getContent(), locationComment.getImage(),
							locationComment.getLocationId(), party.getId());

			given(partyMemberRepository.findByPartyIdAndMemberId(any(Long.class), any(Long.class))).willReturn(
					Optional.of(partyMember));
			given(locationCommentRepository.save(any(LocationComment.class))).willReturn(locationComment);

			// When
			LocationCommentRegisterResponse locationCommentRegisterResponse =
					locationCommentService.registerLocationComment(locationCommentRegisterRequest);

			// Then
			assertThat(locationCommentRegisterResponse.id()).isEqualTo(1L);

		}
	}

	@Nested
	@DisplayName("getAllComments??? ??????")
	class getAllLocationComment {

		@Test
		@DisplayName("????????? ???????????? ????????????.")
		void getAllLocationComments() {
			// Given
			LocationComment locationComment2 = LocationComment.builder()
					.id(2L)
					.image("image.png")
					.content("??? ?????? ??????")
					.locationId(2L)
					.partyMember(partyMember)
					.member(member)
					.build();

			LocationComment locationComment3 = LocationComment.builder()
					.id(3L)
					.image("image.png")
					.content("??? ?????? ??????")
					.locationId(2L)
					.partyMember(partyMember)
					.member(member)
					.build();

			List<LocationComment> locationComments = List.of(locationComment2, locationComment3);
			List<Boolean> isEditables = List.of(true, true);

			LocationCommentGetAllRequest locationCommentGetAllRequest =
					new LocationCommentGetAllRequest(null, 2, 2L);

			LocationCommentGetAllResponse expected = toLocationCommentGetAllResponse(locationComments, isEditables, 3L);

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
	@DisplayName("LocationCommentUpdate??? ?????? ")
	class locationCommentUpdate {
		@Test
		@DisplayName("????????? ????????? ????????????.")
		void success() {
			// Given
			given(locationCommentRepository.findById(any(Long.class))).willReturn(Optional.of(locationComment));
			given(partyMemberRepository.existsById(any(Long.class))).willReturn(true);
			given(locationCommentRepository.save(any(LocationComment.class))).willReturn(locationComment);

			LocationCommentUpdateRequest request =
					new LocationCommentUpdateRequest(locationComment.getId(), "???????????? ???????????? ?????????!!", "image.png");
			LocationCommentUpdateResponse expected =
					new LocationCommentUpdateResponse(locationComment.getId(), "???????????? ???????????? ?????????!!", "image.png");
			// When
			LocationCommentUpdateResponse response = locationCommentService.updateLocationComment(request);

			// Then
			assertThat(response).usingRecursiveComparison().isEqualTo(expected);

		}

		@Nested
		@DisplayName("LocationCommentDelete??? ??????")
		class locationCommentDelete {
			@Test
			@DisplayName("????????? ????????? ????????????.")
			void success() {
				// Given
				LocationComment locationCommentTmp = LocationComment.builder()
						.id(450L)
						.image("image.png")
						.content("????????? ??????....")
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
			@DisplayName("????????? ???????????? ?????? ????????? ????????? ????????? ??? ??????.")
			void failedByOtherMember() {
				// Given
				LocationComment locationCommentTmp = LocationComment.builder()
						.id(450L)
						.image("image.png")
						.content("????????? ??????....")
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
				.description("???????????? ??????")
				.coverImage("party_cover_image.png")
				.name("?????????")
				.endDate(LocalDateTime.now().plusDays(5))
				.startDate(LocalDateTime.now().plusDays(2))
				.build();
	}

	private PartyMember getPartyMember() {
		return PartyMember.builder()
				.id(1L)
				.member(member)
				.party(party)
				.role("??????")
				.isLeader(true)
				.build();
	}

	private LocationComment getLocationComment() {
		return LocationComment.builder()
				.id(1L)
				.image("image.png")
				.content("?????? ??????")
				.locationId(location.getId())
				.partyMember(partyMember)
				.member(member)
				.build();
	}

	private Location getLocation() {
		return Location.builder()
				.id(1L)
				.image("httpL//.png")
				.memberId(member.getId())
				.category(Category.MEAL)
				.description("?????? ??????!! ????????? ?????? ??????!! Must Be StrawBerry!!!")
				.address("??????????????? ?????? ????????? 30 ???????????? ????????? & ???")
				.searchAddress("???????????????")
				.name("????????????")
				.latitude(123.45)
				.longitude(34.45)
				.partyId(party.getId())
				.visitDate(LocalDateTime.now())
				.expectedCost(170000)
				.build();
	}
}
