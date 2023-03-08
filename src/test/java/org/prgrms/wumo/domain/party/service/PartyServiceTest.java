package org.prgrms.wumo.domain.party.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import org.prgrms.wumo.domain.party.dto.request.PartyGetRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyRegisterRequest;
import org.prgrms.wumo.domain.party.dto.request.PartyType;
import org.prgrms.wumo.domain.party.dto.request.PartyUpdateRequest;
import org.prgrms.wumo.domain.party.dto.response.PartyGetAllResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyGetResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyRegisterResponse;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.party.repository.InvitationRepository;
import org.prgrms.wumo.domain.party.repository.PartyMemberRepository;
import org.prgrms.wumo.domain.party.repository.PartyRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@DisplayName("PartyService 의")
@ExtendWith(MockitoExtension.class)
class PartyServiceTest {

	@Mock
	MemberRepository memberRepository;

	@Mock
	PartyRepository partyRepository;

	@Mock
	PartyMemberRepository partyMemberRepository;

	@Mock
	InvitationRepository invitationRepository;

	@InjectMocks
	PartyService partyService;

	//given
	Member member;
	PartyRegisterRequest partyRegisterRequest;
	Party party;
	PartyMember partyMember;

	@BeforeEach
	void setUp() {
		member = Member.builder()
			.id(1L)
			.email("5yes@gmail.com")
			.nickname("오예스오리지널")
			.password("qwe12345")
			.build();

		partyRegisterRequest = new PartyRegisterRequest(
				"오예스 워크샵",
				LocalDate.now(),
				LocalDate.now().plusDays(1),
				"팀 설립 기념 워크샵",
				"https://~.jpeg",
				"총무"
		);

		party = Party.builder()
				.id(1L)
				.name(partyRegisterRequest.name())
				.startDate(LocalDateTime.of(partyRegisterRequest.startDate(), LocalTime.MIN))
				.endDate(LocalDateTime.of(partyRegisterRequest.endDate(), LocalTime.MAX))
				.description(partyRegisterRequest.description())
				.coverImage(partyRegisterRequest.coverImage())
				.build();

		partyMember = PartyMember.builder()
				.id(1L)
				.member(member)
				.party(party)
				.role(partyRegisterRequest.role())
				.isLeader(true)
				.build();

		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(member.getId(), null, Collections.emptyList());
		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

	@Nested
	@DisplayName("registerParty 메소드는 등록 요청시")
	class RegisterParty {

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
					.findById(member.getId());
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

	@Nested
	@DisplayName("getAllParty 메소드는 조회시")
	class GetAllParty {

		@Test
		@DisplayName("사용자가 속한 모임 목록을 반환한다.")
		void success() {
			//mocking
			given(partyMemberRepository.findAllByMemberId(member.getId(), null, 1, PartyType.ALL))
					.willReturn(List.of(partyMember));
			given(partyMemberRepository.countAllByParty(party))
					.willReturn(1L);
			given(partyMemberRepository.findAllByPartyId(party.getId(), null, 3))
					.willReturn(List.of(partyMember));

			//when
			PartyGetAllResponse partyGetAllResponse = partyService.getAllParty(new PartyGetRequest(PartyType.ALL, null, 1));

			//then
			assertThat(partyGetAllResponse.party()).isNotEmpty();
			assertThat(partyGetAllResponse.party().get(0).members()).isNotEmpty();

			then(partyMemberRepository)
					.should()
					.findAllByMemberId(member.getId(), null, 1, PartyType.ALL);
			then(partyMemberRepository)
					.should()
					.countAllByParty(party);
			then(partyMemberRepository)
					.should()
					.findAllByPartyId(party.getId(), null, 3);
		}

		@Test
		@DisplayName("사용자가 속한 모임이 없다면 빈 목록을 반환한다.")
		void empty() {
			//mocking
			given(partyMemberRepository.findAllByMemberId(member.getId(), null, 1, PartyType.ALL))
					.willReturn(Collections.emptyList());

			//when
			PartyGetAllResponse partyGetAllResponse = partyService.getAllParty(new PartyGetRequest(PartyType.ALL, null, 1));

			//then
			assertThat(partyGetAllResponse.party()).isEmpty();
			assertThat(partyGetAllResponse.lastId()).isEqualTo(-1L);

			then(partyMemberRepository)
					.should()
					.findAllByMemberId(member.getId(), null, 1, PartyType.ALL);
		}

	}

	@Nested
	@DisplayName("getParty 메소드는 조회시")
	class GetParty {

		@Test
		@DisplayName("모임의 상세 정보를 반환한다.")
		void success() {
			//mocking
			given(partyRepository.findById(party.getId()))
					.willReturn(Optional.of(party));
			given(partyMemberRepository.countAllByParty(party))
					.willReturn(1L);
			given(partyMemberRepository.findAllByPartyId(party.getId(), null, 3))
					.willReturn(List.of(partyMember));

			//when
			PartyGetResponse myParty = partyService.getParty(party.getId());

			//then
			assertThat(myParty.id()).isEqualTo(party.getId());
			assertThat(myParty.name()).isEqualTo(party.getName());
			assertThat(myParty.startDate()).isEqualTo(LocalDate.from(party.getStartDate()));
			assertThat(myParty.endDate()).isEqualTo(LocalDate.from(party.getEndDate()));
			assertThat(myParty.description()).isEqualTo(party.getDescription());
			assertThat(myParty.coverImage()).isEqualTo(party.getCoverImage());
			assertThat(myParty.totalMembers()).isEqualTo(1L);
			assertThat(myParty.members()).isNotEmpty();

			then(partyRepository)
					.should()
					.findById(party.getId());
			then(partyMemberRepository)
					.should()
					.countAllByParty(party);
			then(partyMemberRepository)
					.should()
					.findAllByPartyId(party.getId(), null, 3);
		}

		@Test
		@DisplayName("존재하지 않는 모임이면 예외가 발생한다.")
		void failed() {
			//mocking
			given(partyRepository.findById(party.getId()))
					.willReturn(Optional.empty());

			//when
			//then
			Assertions.assertThrows(EntityNotFoundException.class, () -> partyService.getParty(party.getId()));
		}

	}

	@Nested
	@DisplayName("updateParty 메소드는 수정시")
	class UpdateParty {
		PartyUpdateRequest partyUpdateRequest = new PartyUpdateRequest(
				"오예스 워크샵 (수정)",
				LocalDate.now(),
				LocalDate.now().plusDays(2),
				"팀 설립 기념 워크샵 (수정)",
				null
		);

		@Test
		@DisplayName("NULL 아닌 필드를 업데이트 한다.")
		void success() {
			//mocking
			given(partyMemberRepository.findByPartyIdAndIsLeader(party.getId()))
					.willReturn(Optional.of(partyMember));
			given(partyRepository.save(party))
					.willReturn(party);

			//when
			PartyGetResponse partyGetResponse = partyService.updateParty(party.getId(), partyUpdateRequest);

			//then
			assertThat(partyGetResponse.id()).isEqualTo(party.getId());
			assertThat(partyGetResponse.name()).isEqualTo(partyUpdateRequest.name());
			assertThat(partyGetResponse.startDate()).isEqualTo(partyUpdateRequest.startDate());
			assertThat(partyGetResponse.endDate()).isEqualTo(partyUpdateRequest.endDate());
			assertThat(partyGetResponse.description()).isEqualTo(partyUpdateRequest.description());
			assertThat(partyGetResponse.coverImage()).isEqualTo(party.getCoverImage());

			then(partyMemberRepository)
					.should()
					.findByPartyIdAndIsLeader(party.getId());
			then(partyRepository)
					.should()
					.save(any(Party.class));
		}

		@Test
		@DisplayName("종료일이 시작일보다 빠르면 예외가 발생한다.")
		void invalidDate() {
			//given
			PartyUpdateRequest wrongRequest = new PartyUpdateRequest(
					null,
					LocalDate.now().plusDays(1),
					LocalDate.now(),
					null,
					null
			);

			//mocking
			given(partyMemberRepository.findByPartyIdAndIsLeader(party.getId()))
					.willReturn(Optional.of(partyMember));

			//when
			//then
			Assertions.assertThrows(IllegalArgumentException.class,
					() -> partyService.updateParty(partyMember.getParty().getId(), wrongRequest));
		}

		@Test
		@DisplayName("존재하지 않는 모임이면 예외가 발생한다.")
		void failed() {
			//mocking
			given(partyMemberRepository.findByPartyIdAndIsLeader(party.getId()))
					.willReturn(Optional.empty());

			//when
			//then
			Assertions.assertThrows(EntityNotFoundException.class,
					() -> partyService.updateParty(party.getId(), partyUpdateRequest));
		}

	}

	@Nested
	@DisplayName("deleteParty 메소드는 삭제시")
	class DeleteParty {

		@Test
		@DisplayName("식별자가 일치하는 모임을 삭제한다.")
		void success() {
			//mocking
			given(partyMemberRepository.findByPartyIdAndIsLeader(party.getId()))
					.willReturn(Optional.of(partyMember));

			//when
			partyService.deleteParty(party.getId());

			//then
			then(partyMemberRepository)
					.should()
					.findByPartyIdAndIsLeader(party.getId());
			then(partyRepository)
					.should()
					.delete(party);
		}

		@Test
		@DisplayName("존재하지 않는 모임이면 예외가 발생한다.")
		void failed() {
			//mocking
			given(partyMemberRepository.findByPartyIdAndIsLeader(party.getId()))
					.willReturn(Optional.empty());

			//when
			//then
			Assertions.assertThrows(EntityNotFoundException.class, () -> partyService.deleteParty(party.getId()));
		}

	}

}