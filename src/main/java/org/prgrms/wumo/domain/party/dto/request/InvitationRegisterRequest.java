package org.prgrms.wumo.domain.party.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "초대 생성 요청 정보")
public record InvitationRegisterRequest(

		@FutureOrPresent(message = "만료일은 현재보다 과거일 수 없습니다.")
		@Schema(description = "만료일 (Default = Current + 7 Days)", example = "2023-02-22", required = true)
		LocalDate expiredDate

) {
	public InvitationRegisterRequest {
		expiredDate = (expiredDate == null) ? LocalDate.now().plusDays(7) : expiredDate;
	}
}
