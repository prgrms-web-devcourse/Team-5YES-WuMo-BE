package org.prgrms.wumo.domain.comment.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentGetAllResponse;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.model.LocationComment;
import org.prgrms.wumo.domain.comment.repository.LocationCommentRepository;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
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

	@BeforeEach
	void beforeEach() {
		member = getMember();
		party = getParty();
		partyMember = getPartyMember();
		locationComment = getLocationComment();

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
					.isEdited(false)
					.partyMember(partyMember)
					.member(member)
					.build();

			LocationComment locationComment3 = LocationComment.builder()
					.id(3L)
					.image("image.png")
					.content("두 번째 댓글")
					.locationId(2L)
					.isEdited(false)
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

	private LocationComment getLocationComment() {
		return LocationComment.builder()
				.id(1L)
				.image("image.png")
				.content("댓글 댓글")
				.locationId(1L)
				.isEdited(false)
				.partyMember(partyMember)
				.member(member)
				.build();
	}
}
