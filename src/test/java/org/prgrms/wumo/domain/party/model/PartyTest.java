package org.prgrms.wumo.domain.party.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Party 엔티티를 생성할 때")
class PartyTest {

	@Test
	@DisplayName("모임 시작일보다 모임 종료일이 빠르면 예외가 발생한다.")
	void checkEndDateIsBeforeStartDate() {
		assertThrows(IllegalArgumentException.class, () -> Party.builder()
						.startDate(LocalDateTime.now())
						.endDate(LocalDateTime.now().minusDays(1))
						.build());
	}

}