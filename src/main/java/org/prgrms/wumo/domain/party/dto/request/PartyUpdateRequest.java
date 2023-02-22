package org.prgrms.wumo.domain.party.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 생성 요청 정보")
public record PartyUpdateRequest(

		@NotBlank(message = "모임 이름은 필수 입력사항입니다.")
		@Length(max = 40, message = "모임 이름은 {max}자를 초과할 수 없습니다.")
		@Schema(description = "모임 이름", example = "오예스 워크샵 (수정)", required = false)
		String name,

		@NotNull(message = "시작일은 필수 입력사항입니다.")
		@Schema(description = "시작일", example = "2023-02-21", required = false)
		LocalDate startDate,

		@NotNull(message = "종료일은 필수 입력사항입니다.")
		@Schema(description = "종료일", example = "2023-02-22", required = false)
		LocalDate endDate,

		@Length(max = 255, message = "모임 설명은 {max}자를 초과할 수 없습니다.")
		@Schema(description = "종료일", example = "팀 설립 기념 워크샵", required = false)
		String description,

		@Length(max = 255, message = "이미지 경로는 {max}자를 초과할 수 없습니다.")
		@Schema(description = "이미지 경로", example = "https://~.jpeg", required = false)
		String coverImage,

		@Length(min = 4, max = 4, message = "비밀번호는 {min}자리가 필요합니다.")
		@Schema(description = "입장 비밀번호", example = "1234", required = false)
		String password
) {
}
