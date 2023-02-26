package org.prgrms.wumo.domain.party.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.dto.request.PartyRegisterRequest;
import org.prgrms.wumo.domain.party.dto.response.PartyRegisterResponse;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.party.repository.PartyRepository;

@DisplayName("PartyService 의")
@ExtendWith(MockitoExtension.class)
class PartyServiceTest {

	@Mock
	MemberRepository memberRepository;

	@Mock
	PartyRepository partyRepository;

	@Mock
	PartyMemberRepository partyMemberRepository;

	@InjectMocks
	PartyService partyService;

	@Nested
	@DisplayName("registerParty 메소드는 등록 요청시")
	class RegisterParty {
		//given
		Member member = Member.builder()
				.id(1L)
				.email("5yes@gmail.com")
				.nickname("오예스오리지널")
				.password("qwe12345")
				.build();
		PartyRegisterRequest partyRegisterRequest = new PartyRegisterRequest(
				"오예스 워크샵",
				LocalDateTime.now(),
				LocalDateTime.now().plusDays(1),
				"팀 설립 기념 워크샵",
				"https://~.jpeg",
				"1234",
				1L,
				"총무"
		);
		Party party = Party.builder()
				.id(1L)
				.name(partyRegisterRequest.name())
				.startDate(partyRegisterRequest.startDate())
				.endDate(partyRegisterRequest.endDate())
				.description(partyRegisterRequest.description())
				.coverImage(partyRegisterRequest.coverImage())
				.password(partyRegisterRequest.password())
				.build();
		PartyMember partyMember = PartyMember.builder()
				.member(member)
				.party(party)
				.role(partyRegisterRequest.role())
				.isLeader(true)
				.build();

		@Test
		@DisplayName("모임을 생성하고 생성을 요청한 사용자를 파티장으로 등록한다.")
		void success() {
			//mocking
			given(partyRepository.save(any(Party.class)))
					.willReturn(party);
			given(memberRepository.findById(member.getId()))
					.willReturn(Optional.of(member));
			given(partyMemberRepository.save(any(PartyMember.class)))
					.willReturn(partyMember);

			//when
			PartyRegisterResponse response = partyService.registerParty(partyRegisterRequest);

			//then
			then(partyRepository)
					.should()
					.save(any(Party.class));
			then(memberRepository)
					.should()
					.findById(any(Long.class));
			then(partyMemberRepository)
					.should()
					.save(any(PartyMember.class));

			assertThat(response.id()).isEqualTo(party.getId());
		}
		@Test
		@DisplayName("유효하지 않은 사용자면 예외가 발생한다.")
		void failed() {
			//mocking
			given(partyRepository.save(any(Party.class)))
					.willReturn(party);
			given(memberRepository.findById(member.getId()))
					.willReturn(Optional.empty());

			//when
			//then
			Assertions.assertThrows(EntityNotFoundException.class, () -> partyService.registerParty(partyRegisterRequest));

			then(partyMemberRepository)
					.should(never())
					.save(any(PartyMember.class));
		}

	}

}