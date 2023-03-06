package org.prgrms.wumo.domain.party.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Party 엔티티를")
class PartyTest {

	@Test
	@DisplayName("생성할 때 모임 시작일보다 모임 종료일이 빠르면 예외가 발생한다.")
	void checkEndDateIsBeforeStartDateOnRegister() {
		assertThrows(IllegalArgumentException.class, () -> Party.builder()
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().minusDays(1))
				.build());
	}

	@Test
	@DisplayName("수정할 때 모임 시작일보다 모임 종료일이 빠르면 예외가 발생한다.")
	void checkEndDateIsBeforeStartDateOnUpdate() {
		Party party = Party.builder()
				.name("오예스 워크샵")
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plusDays(1))
				.description("팀 설립 기념 워크샵")
				.coverImage("https://~.jpeg")
				.password("1234")
				.build();

		assertThrows(IllegalArgumentException.class, () -> party.update(
				null,
				LocalDateTime.now(),
				LocalDateTime.now().minusDays(1),
				null,
				null,
				null
		));
	}

}