package org.prgrms.wumo.domain.party.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
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
import org.prgrms.wumo.domain.party.dto.request.InvitationRegisterRequest;
import org.prgrms.wumo.domain.party.dto.response.InvitationRegisterResponse;
import org.prgrms.wumo.domain.party.model.Invitation;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.InvitationRepository;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
@DisplayName("InvitationService 의")
class InvitationServiceTest {

	@Mock
	PartyMemberRepository partyMemberRepository;

	@Mock
	InvitationRepository invitationRepository;

	@InjectMocks
	InvitationService invitationService;

	//given
	Member member;
	Party party;
	PartyMember partyMember;
	Invitation invitation;

	@BeforeEach
	void setUp() {
		member = Member.builder()
				.id(1L)
				.email("5yes@gmail.com")
				.nickname("오예스오리지널")
				.password("qwe12345")
				.build();
		party = Party.builder()
				.id(1L)
				.name("오예스 워크샵")
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plusDays(1))
				.description("팀 설립 기념 워크샵")
				.coverImage("https://~.jpeg")
				.password("1234")
				.build();
		partyMember = PartyMember.builder()
				.id(1L)
				.member(member)
				.party(party)
				.role("총무")
				.isLeader(true)
				.build();
		invitation = Invitation.builder()
				.id(10000000L)
				.party(party)
				.expiredDate(LocalDateTime.now().plusDays(7))
				.build();

		setAuthentication(member.getId());
	}

	@Nested
	@DisplayName("registerInvitation 메소드는 등록 요청시")
	class RegisterInvitation {

		@Test
		@DisplayName("모임 구성원이고 생성한 코드가 없다면 초대코드를 생성한다.")
		void successIfNotExists() {
			//given
			InvitationRegisterRequest invitationRegisterRequest = new InvitationRegisterRequest(LocalDate.now().plusDays(7));

			//mocking
			given(partyMemberRepository.findByPartyIdAndMemberId(party.getId(), member.getId()))
					.willReturn(Optional.of(partyMember));
			given(invitationRepository.findTopByPartyOrderByIdDesc(party))
					.willReturn(Optional.empty());
			given(invitationRepository.save(any(Invitation.class)))
					.willReturn(invitation);

			//when
			InvitationRegisterResponse invitationRegisterResponse
					= invitationService.registerInvitation(party.getId(), invitationRegisterRequest);

			//then
			assertThat(invitationRegisterResponse.code()).isEqualTo(invitation.getCode());

			then(partyMemberRepository)
					.should()
					.findByPartyIdAndMemberId(party.getId(), member.getId());
			then(invitationRepository)
					.should()
					.findTopByPartyOrderByIdDesc(party);
			then(invitationRepository)
					.should()
					.save(any(Invitation.class));
		}

		@Test
		@DisplayName("모임 구성원이고 만료된 초대장이 있다면 초대코드를 새로 생성한다.")
		void successIfAlreadyExists() {
			//given
			Invitation oldInvitation = Invitation.builder()
					.id(10000001L)
					.party(party)
					.expiredDate(LocalDateTime.now().minusDays(7))
					.build();
			InvitationRegisterRequest invitationRegisterRequest = new InvitationRegisterRequest(LocalDate.now().plusDays(7));

			//mocking
			given(partyMemberRepository.findByPartyIdAndMemberId(party.getId(), member.getId()))
					.willReturn(Optional.of(partyMember));
			given(invitationRepository.findTopByPartyOrderByIdDesc(party))
					.willReturn(Optional.of(oldInvitation));
			given(invitationRepository.save(any(Invitation.class)))
					.willReturn(invitation);

			//when
			InvitationRegisterResponse invitationRegisterResponse
					= invitationService.registerInvitation(party.getId(), invitationRegisterRequest);

			//then
			assertThat(invitationRegisterResponse.code()).isEqualTo(invitation.getCode());

			then(partyMemberRepository)
					.should()
					.findByPartyIdAndMemberId(party.getId(), member.getId());
			then(invitationRepository)
					.should()
					.findTopByPartyOrderByIdDesc(party);
			then(invitationRepository)
					.should()
					.save(any(Invitation.class));
		}

		@Test
		@DisplayName("모임 구성원이고 만료되지 않은 초대장이 있다면 기존 초대코드를 반환한다.")
		void successIfAlreadyExistsButNotExpired() {
			//given
			InvitationRegisterRequest invitationRegisterRequest = new InvitationRegisterRequest(LocalDate.now().plusDays(7));

			//mocking
			given(partyMemberRepository.findByPartyIdAndMemberId(party.getId(), member.getId()))
					.willReturn(Optional.of(partyMember));
			given(invitationRepository.findTopByPartyOrderByIdDesc(party))
					.willReturn(Optional.of(invitation));

			//when
			InvitationRegisterResponse invitationRegisterResponse
					= invitationService.registerInvitation(party.getId(), invitationRegisterRequest);

			//then
			assertThat(invitationRegisterResponse.code()).isEqualTo(invitation.getCode());

			then(partyMemberRepository)
					.should()
					.findByPartyIdAndMemberId(party.getId(), member.getId());
			then(invitationRepository)
					.should()
					.findTopByPartyOrderByIdDesc(party);
			then(invitationRepository)
					.should(never())
					.save(any(Invitation.class));
		}

		@Test
		@DisplayName("모임 구성원이 아니라면 예외가 발생한다.")
		void failed() {
			//given
			InvitationRegisterRequest invitationRegisterRequest = new InvitationRegisterRequest(LocalDate.now().plusDays(7));

			//mocking
			given(partyMemberRepository.findByPartyIdAndMemberId(party.getId(), member.getId()))
					.willReturn(Optional.empty());

			//when
			//then
			Assertions.assertThrows(EntityNotFoundException.class,
					() -> invitationService.registerInvitation(party.getId(), invitationRegisterRequest));
		}

	}

	private void setAuthentication(Long memberId) {
		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(memberId, null, Collections.emptyList());
		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

}