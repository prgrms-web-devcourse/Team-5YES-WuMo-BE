package org.prgrms.wumo.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Collections;
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
import org.prgrms.wumo.domain.comment.dto.request.ReplyCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.response.ReplyCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.model.LocationComment;
import org.prgrms.wumo.domain.comment.model.ReplyComment;
import org.prgrms.wumo.domain.comment.repository.LocationCommentRepository;
import org.prgrms.wumo.domain.comment.repository.ReplyCommentRepository;
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

@DisplayName("ReplyCommentService 에서")
@ExtendWith(MockitoExtension.class)
public class ReplyCommentServiceTest {

	@InjectMocks
	ReplyCommentService replyCommentService;

	@Mock
	LocationCommentRepository locationCommentRepository;

	@Mock
	PartyMemberRepository partyMemberRepository;

	@Mock
	ReplyCommentRepository replyCommentRepository;

	Member member;
	LocationComment locationComment;
	Party party;
	PartyMember partyMember;
	Location location;
	ReplyComment replyComment;

	@BeforeEach
	void beforeEach() {
		member = getMember();
		party = getParty();
		partyMember = getPartyMember();
		location = getLocation();
		locationComment = getLocationComment();
		replyComment = getReplyComment();

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
	@DisplayName("registerReplyComment 를 통해")
	class RegisterReplyComment {
		@Test
		@DisplayName("후보지 댓글에 대댓글을 작성할 수 있다.")
		void success() {
			// Given
			ReplyCommentRegisterRequest replyCommentRegisterRequest =
				new ReplyCommentRegisterRequest(locationComment.getId(), "대댓글 쓰자!!!");

			given(locationCommentRepository.findById(any(Long.class))).willReturn(Optional.of(locationComment));
			given(partyMemberRepository.existsById(any(Long.class))).willReturn(true);
			given(replyCommentRepository.save(any())).willReturn(replyComment);

			// When
			ReplyCommentRegisterResponse replyCommentRegisterResponse =
					replyCommentService.registerReplyComment(replyCommentRegisterRequest);

			// Then
			assertThat(replyCommentRegisterResponse.id()).isEqualTo(1L);
		}

		@Test
		@DisplayName("모임의 일원이 아니면 대댓글을 작성할 수 없다.")
		void failedWithUnAuthorized() {
			// Given
			ReplyCommentRegisterRequest replyCommentRegisterRequest =
				new ReplyCommentRegisterRequest(locationComment.getId(), "대댓글 쓰자!!!");

			given(locationCommentRepository.findById(any(Long.class))).willReturn(Optional.of(locationComment));
			given(partyMemberRepository.existsById(any(Long.class))).willReturn(false);
			//given(replyCommentRepository.save(any())).willReturn(replyComment);

			// When // Then
			assertThatThrownBy(
					() -> replyCommentService.registerReplyComment(replyCommentRegisterRequest)
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
				.memberId(member.getId())
				.category(Category.MEAL)
				.description("딸기 뷔페!! 드디어 예약 성공!! Must Be StrawBerry!!!")
				.address("서울특별시 중구 을지로 30 페닌슐라 라운지 & 바")
				.searchAddress("서울특별시")
				.name("롯데호텔")
				.latitude(123.45)
				.longitude(34.45)
				.partyId(party.getId())
				.visitDate(LocalDateTime.now())
				.expectedCost(170000)
				.build();
	}

	private ReplyComment getReplyComment() {
		return ReplyComment.builder()
				.id(1L)
				.member(member)
				.commentId(locationComment.getId())
				.content("대댓글!!!")
				.build();
	}
}
