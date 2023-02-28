package org.prgrms.wumo.domain.party.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import org.prgrms.wumo.domain.party.dto.response.PartyMemberGetAllResponse;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.party.repository.PartyRepository;
import org.prgrms.wumo.global.exception.custom.DuplicateException;
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
				.password("1234")
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

		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(participant.getId(), null, Collections.emptyList());
		context.setAuthentication(usernamePasswordAuthenticationToken);
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
		PartyMemberGetRequest partyMemberGetRequest = new PartyMemberGetRequest(null, 1);

		@Test
		@DisplayName("현재 파티의 구성원 목록을 반환한다.")
		void success() {

			//mocking
			given(partyMemberRepository.findAllByPartyId(party.getId(), null, 1))
					.willReturn(List.of(partyLeader, partyParticipant));

			//when
			PartyMemberGetAllResponse partyMemberGetAllResponse
					= partyMemberService.getAllPartyMembers(party.getId(), partyMemberGetRequest);

			//then
			assertThat(partyMemberGetAllResponse.members().size()).isEqualTo(2);
			assertThat(partyMemberGetAllResponse.lastId()).isEqualTo(partyParticipant.getId());

			then(partyMemberRepository)
					.should()
					.findAllByPartyId(party.getId(), null, 1);
		}

	}

}