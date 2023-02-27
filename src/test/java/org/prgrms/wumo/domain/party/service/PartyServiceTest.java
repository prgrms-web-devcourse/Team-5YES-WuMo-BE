package org.prgrms.wumo.domain.party.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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
import org.prgrms.wumo.domain.party.dto.request.PartyUpdateRequest;
import org.prgrms.wumo.domain.party.dto.response.PartyGetAllResponse;
import org.prgrms.wumo.domain.party.dto.response.PartyGetDetailResponse;
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

	@Nested
	@DisplayName("getAllParty 메소드는 조회시")
	class GetAllParty {

		@Test
		@DisplayName("사용자가 속한 모임 목록을 반환한다.")
		void success() {
			//mocking
			given(partyMemberRepository.findAllByMember(member))
					.willReturn(List.of(partyMember));
			given(memberRepository.findById(member.getId()))
					.willReturn(Optional.of(member));

			//when
			PartyGetAllResponse myParty = partyService.getAllParty(member.getId(), null);

			//then
			assertThat(myParty.party()).isNotEmpty();

			then(partyMemberRepository)
					.should()
					.findAllByMember(member);
			then(memberRepository)
					.should()
					.findById(member.getId());
		}

		@Test
		@DisplayName("사용자가 속한 모임이 없다면 빈 목록을 반환한다.")
		void empty() {
			//mocking
			given(partyMemberRepository.findAllByMember(member))
					.willReturn(Collections.emptyList());
			given(memberRepository.findById(member.getId()))
					.willReturn(Optional.of(member));

			//when
			PartyGetAllResponse myEmptyParty = partyService.getAllParty(member.getId(), null);

			//then
			assertThat(myEmptyParty.party()).isEmpty();

			then(partyMemberRepository)
					.should()
					.findAllByMember(member);
			then(memberRepository)
					.should()
					.findById(member.getId());
		}

		@Test
		@DisplayName("존재하지 않는 사용자라면 예외가 발생한다.")
		void failed() {
			//mocking
			given(memberRepository.findById(member.getId()))
					.willReturn(Optional.empty());

			//when
			//then
			Assertions.assertThrows(EntityNotFoundException.class, () -> partyService.getAllParty(member.getId(), null));
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

			//when
			PartyGetDetailResponse myParty = partyService.getParty(party.getId());

			//then
			assertThat(myParty.id()).isEqualTo(party.getId());
			assertThat(myParty.name()).isEqualTo(party.getName());
			assertThat(myParty.startDate()).isEqualTo(party.getStartDate());
			assertThat(myParty.endDate()).isEqualTo(party.getEndDate());
			assertThat(myParty.description()).isEqualTo(party.getDescription());
			assertThat(myParty.coverImage()).isEqualTo(party.getCoverImage());

			then(partyRepository)
					.should()
					.findById(party.getId());
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
				LocalDateTime.now(),
				LocalDateTime.now().plusDays(2),
				"팀 설립 기념 워크샵 (수정)",
				null,
				"4321"
		);

		@Test
		@DisplayName("NULL 아닌 필드를 업데이트 한다.")
		void success() {
			//mocking
			given(partyRepository.findById(party.getId()))
					.willReturn(Optional.of(party));
			given(partyRepository.save(party))
					.willReturn(party);

			//when
			PartyGetDetailResponse partyGetDetailResponse = partyService.updateParty(party.getId(), partyUpdateRequest);

			//then
			assertThat(partyGetDetailResponse.id()).isEqualTo(party.getId());
			assertThat(partyGetDetailResponse.name()).isEqualTo(partyUpdateRequest.name());
			assertThat(partyGetDetailResponse.startDate()).isEqualTo(partyUpdateRequest.startDate());
			assertThat(partyGetDetailResponse.endDate()).isEqualTo(partyUpdateRequest.endDate());
			assertThat(partyGetDetailResponse.description()).isEqualTo(partyUpdateRequest.description());
			assertThat(partyGetDetailResponse.coverImage()).isEqualTo(party.getCoverImage());

			then(partyRepository)
					.should()
					.findById(party.getId());
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
					LocalDateTime.now().plusDays(1),
					LocalDateTime.now(),
					null,
					null,
					null
			);

			//mocking
			given(partyRepository.findById(party.getId()))
					.willReturn(Optional.of(party));

			//when
			//then
			Assertions.assertThrows(IllegalArgumentException.class, () -> partyService.updateParty(party.getId(), wrongRequest));
		}

		@Test
		@DisplayName("존재하지 않는 모임이면 예외가 발생한다.")
		void failed() {
			//mocking
			given(partyRepository.findById(party.getId()))
					.willReturn(Optional.empty());

			//when
			//then
			Assertions.assertThrows(EntityNotFoundException.class, () -> partyService.updateParty(party.getId(), partyUpdateRequest));
		}

	}

	@Nested
	@DisplayName("deleteParty 메소드는 삭제시")
	class DeleteParty {

		@Test
		@DisplayName("식별자가 일치하는 모임을 삭제한다.")
		void success() {
			//mocking
			given(partyRepository.findById(party.getId()))
					.willReturn(Optional.of(party));

			//when
			partyService.deleteParty(party.getId());

			//then
			then(partyRepository)
					.should()
					.findById(party.getId());
			then(partyRepository)
					.should()
					.delete(party);
		}

		@Test
		@DisplayName("존재하지 않는 모임이면 예외가 발생한다.")
		void failed() {
			//mocking
			given(partyRepository.findById(party.getId()))
					.willReturn(Optional.empty());

			//when
			//then
			Assertions.assertThrows(EntityNotFoundException.class, () -> partyService.deleteParty(party.getId()));
		}

	}

}