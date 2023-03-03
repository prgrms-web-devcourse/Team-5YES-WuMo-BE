package org.prgrms.wumo.domain.party.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "초대 생성 요청 정보")
public record InvitationRegisterRequest(

	@NotNull(message = "만료일은 필수 입력사항입니다.")
	@FutureOrPresent(message = "만료일은 현재보다 과거일 수 없습니다.")
	@Schema(description = "만료일", example = "2023-02-22", requiredMode = Schema.RequiredMode.REQUIRED)
	LocalDate expiredDate

) {
}
