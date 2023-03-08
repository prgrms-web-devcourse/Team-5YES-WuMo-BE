package org.prgrms.wumo.domain.party.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.dto.request.PartyMemberGetRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyMemberRegisterRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyMemberUpdateRequest;
import org.prgrms.wumo.domain.party.dto.response.PartyMemberGetAllResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyMemberGetResponse;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.InvitationRepository;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.party.repository.PartyRepository;
import org.prgrms.wumo.global.exception.custom.DuplicateException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
@DisplayName("PartyMemberService 의")
class PartyMemberServiceTest {

	@Mock
	MemberRepository memberRepository;

	@Mock
	PartyRepository partyRepository;

	@Mock
	PartyMemberRepository partyMemberRepository;

	@Mock
	InvitationRepository invitationRepository;

	@InjectMocks
	PartyMemberService partyMemberService;

	//given
	Member leader;
	Member participant;
	PartyMemberRegisterRequest partyMemberRegisterRequest;
	Party party;
	PartyMember partyLeader;
	PartyMember partyParticipant;

	@BeforeEach
	void setUp() {
		leader = Member.builder()
				.id(1L)
				.email("5yes@gmail.com")
				.nickname("오예스오리지널")
				.password("qwe12345")
				.build();
		participant = Member.builder()
				.id(2L)
				.email("ted@gmail.com")
				.nickname("테드창")
				.password("qwe12345")
				.build();
		party = Party.builder()
				.id(1L)
				.name("오예스 워크샵")
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plusDays(1))
				.description("팀 설립 기념 워크샵")
				.coverImage("https://~.jpeg")
				.build();
		partyLeader = PartyMember.builder()
				.id(1L)
				.member(leader)
				.party(party)
				.role("총무")
				.isLeader(true)
				.build();
		partyParticipant = PartyMember.builder()
				.id(2L)
				.member(participant)
				.party(party)
				.role("운전기사")
				.isLeader(false)
				.build();
		partyMemberRegisterRequest = new PartyMemberRegisterRequest("운전기사");

		setAuthentication(participant.getId());
	}

	@Nested
	@DisplayName("registerPartyMember 메소드는 등록 요청시")
	class RegisterPartyMember {

		@Test
		@DisplayName("모임 구성원에 현재 로그인 된 사용자를 등록한다.")
		void success() {
			//mocking
			given(partyMemberRepository.existsByPartyIdAndMemberId(party.getId(), participant.getId()))
					.willReturn(false);
			given(memberRepository.findById(participant.getId()))
					.willReturn(Optional.of(participant));
			given(partyRepository.findById(party.getId()))
					.willReturn(Optional.of(party));

			//when
			partyMemberService.registerPartyMember(party.getId(), partyMemberRegisterRequest);

			//then
			then(partyMemberRepository)
					.should()
					.existsByPartyIdAndMemberId(party.getId(), participant.getId());
			then(memberRepository)
					.should()
					.findById(participant.getId());
			then(partyRepository)
					.should()
					.findById(party.getId());
			then(partyMemberRepository)
					.should()
					.save(any(PartyMember.class));
		}

		@Test
		@DisplayName("이미 모임에 참여한 사용자면 예외가 발생한다.")
		void failed() {
			//mocking
			given(partyMemberRepository.existsByPartyIdAndMemberId(party.getId(), participant.getId()))
					.willReturn(true);

			//when
			//then
			Assertions.assertThrows(DuplicateException.class,
					() -> partyMemberService.registerPartyMember(party.getId(), partyMemberRegisterRequest));
		}

	}

	@Nested
	@DisplayName("getAllPartyMembers 메소드는 조회시")
	class GetAllPartyMembers {
		//given
		PartyMemberGetRequest partyMemberGetRequest = new PartyMemberGetRequest(null, 2);

		@Test
		@DisplayName("현재 모임의 구성원 목록을 반환한다.")
		void success() {
			//mocking
			given(partyMemberRepository.findByPartyIdAndMemberId(party.getId(), participant.getId()))
					.willReturn(Optional.of(partyParticipant));
			given(partyMemberRepository.findAllByPartyId(party.getId(), null, 2))
					.willReturn(List.of(partyLeader, partyParticipant));

			//when
			PartyMemberGetAllResponse partyMemberGetAllResponse
					= partyMemberService.getAllPartyMembers(party.getId(), partyMemberGetRequest);

			//then
			assertThat(partyMemberGetAllResponse.members().size()).isEqualTo(2);
			assertThat(partyMemberGetAllResponse.lastId()).isEqualTo(partyParticipant.getId());

			then(partyMemberRepository)
					.should()
					.findByPartyIdAndMemberId(party.getId(), participant.getId());
			then(partyMemberRepository)
					.should()
					.findAllByPartyId(party.getId(), null, 2);
		}

		@Test
		@DisplayName("모임의 구성원이 아니라면 예외가 발생한다.")
		void failed() {
			//mocking
			given(partyMemberRepository.findByPartyIdAndMemberId(party.getId(), participant.getId()))
					.willReturn(Optional.empty());

			//when
			//then
			Assertions.assertThrows(EntityNotFoundException.class,
					() -> partyMemberService.getAllPartyMembers(party.getId(), partyMemberGetRequest));
		}

	}

	@Nested
	@DisplayName("updatePartyMember 메소드는 수정시")
	class UpdatePartyMember {
		//given
		PartyMemberUpdateRequest partyMemberUpdateRequest = new PartyMemberUpdateRequest("드라이버");

		@Test
		@DisplayName("현재 로그인한 사용자의 모임에서 정보를 수정한다.")
		void success() {
			//mocking
			given(partyMemberRepository.findByPartyIdAndMemberId(party.getId(), participant.getId()))
					.willReturn(Optional.of(partyParticipant));
			given(partyMemberRepository.save(partyParticipant))
					.willReturn(partyParticipant);

			//when
			PartyMemberGetResponse partyMemberGetResponse
					= partyMemberService.updatePartyMember(party.getId(), partyMemberUpdateRequest);

			//then
			assertThat(partyMemberGetResponse.memberId()).isEqualTo(participant.getId());
			assertThat(partyMemberGetResponse.nickname()).isEqualTo(participant.getNickname());
			assertThat(partyMemberGetResponse.role()).isEqualTo(partyMemberUpdateRequest.role());
			assertThat(partyMemberGetResponse.profileImage()).isEqualTo(participant.getProfileImage());

			then(partyMemberRepository)
					.should()
					.findByPartyIdAndMemberId(party.getId(), participant.getId());
			then(partyMemberRepository)
					.should()
					.save(any(PartyMember.class));
		}

		@Test
		@DisplayName("모임에 가입된 회원이 아니면 예외가 발생한다.")
		void failed() {
			//mocking
			given(partyMemberRepository.findByPartyIdAndMemberId(party.getId(), participant.getId()))
					.willReturn(Optional.empty());

			//when
			//then
			Assertions.assertThrows(EntityNotFoundException.class,
					() -> partyMemberService.updatePartyMember(party.getId(), partyMemberUpdateRequest));

			then(partyMemberRepository)
					.should()
					.findByPartyIdAndMemberId(party.getId(), participant.getId());
		}

	}

	@Nested
	@DisplayName("deletePartyMember 메소드는 호출시")
	class DeletePartyMember {

		@Test
		@DisplayName("모임을 생성한 회원이 아니라면 모임에서 자신을 삭제한다.")
		void successWithNotLeader() {
			//mocking
			given(partyMemberRepository.findByPartyIdAndMemberId(party.getId(), participant.getId()))
					.willReturn(Optional.of(partyParticipant));

			//when
			partyMemberService.deletePartyMember(party.getId());

			//then
			then(partyMemberRepository)
					.should()
					.findByPartyIdAndMemberId(party.getId(), participant.getId());
			then(partyMemberRepository)
					.should()
					.delete(any(PartyMember.class));
		}

		@Test
		@DisplayName("모임을 생성한 회원이면 다른 구성원을 추방할 수 있다.")
		void successKickWithLeader() {
			//given
			setAuthentication(leader.getId());

			//mocking
			given(partyMemberRepository.findByPartyIdAndMemberId(party.getId(), leader.getId()))
					.willReturn(Optional.of(partyLeader));
			given(partyMemberRepository.findByPartyIdAndMemberId(party.getId(), participant.getId()))
					.willReturn(Optional.of(partyParticipant));

			//when
			partyMemberService.deletePartyMember(party.getId(), participant.getId());

				//then
			then(partyMemberRepository)
					.should()
					.findByPartyIdAndMemberId(party.getId(), leader.getId());
			then(partyMemberRepository)
					.should()
					.findByPartyIdAndMemberId(party.getId(), participant.getId());
			then(partyMemberRepository)
					.should()
					.delete(any(PartyMember.class));
		}

		@Test
		@DisplayName("모임을 생성한 회원은 자신을 추방할 수 없다.")
		void failedSelfKickWithLeader() {
			//given
			setAuthentication(leader.getId());

			//mocking
			given(partyMemberRepository.findByPartyIdAndMemberId(party.getId(), leader.getId()))
					.willReturn(Optional.of(partyLeader));

			//when
			//then
			Assertions.assertThrows(AccessDeniedException.class,
					() -> partyMemberService.deletePartyMember(party.getId(), leader.getId()));

			then(partyMemberRepository)
					.should()
					.findByPartyIdAndMemberId(party.getId(), leader.getId());
		}

		@Test
		@DisplayName("모임을 생성한 회원이 아니면 다른 구성원을 추방시 예외가 발생한다.")
		void failedKickWithParticipant() {
			//mocking
			given(partyMemberRepository.findByPartyIdAndMemberId(party.getId(), participant.getId()))
					.willReturn(Optional.of(partyParticipant));

			//when
			//then
			Assertions.assertThrows(AccessDeniedException.class,
					() -> partyMemberService.deletePartyMember(party.getId(), leader.getId()));
		}

	}

	private void setAuthentication(Long memberId) {
		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(memberId, null, Collections.emptyList());
		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

}