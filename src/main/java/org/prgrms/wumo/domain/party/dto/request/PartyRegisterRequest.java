package org.prgrms.wumo.domain.party.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 생성 요청 정보")
public record PartyRegisterRequest(

		@NotBlank(message = "모임 이름은 필수 입력사항입니다.")
		@Length(max = 40, message = "모임 이름은 {max}자를 초과할 수 없습니다.")
		@Schema(description = "모임 이름", example = "오예스 워크샵", requiredMode = Schema.RequiredMode.REQUIRED)
		String name,

		@NotNull(message = "시작일은 필수 입력사항입니다.")
		@Schema(description = "시작일", example = "2023-02-21", requiredMode = Schema.RequiredMode.REQUIRED)
		LocalDate startDate,

		@NotNull(message = "종료일은 필수 입력사항입니다.")
		@Schema(description = "종료일", example = "2023-02-22", requiredMode = Schema.RequiredMode.REQUIRED)
		LocalDate endDate,

		@NotBlank(message = "모임 설명은 필수 입력사항입니다.")
		@Length(max = 255, message = "모임 설명은 {max}자를 초과할 수 없습니다.")
		@Schema(description = "모임 설명", example = "팀 설립 기념 워크샵", requiredMode = Schema.RequiredMode.REQUIRED)
		String description,

		@Length(max = 255, message = "이미지 경로는 {max}자를 초과할 수 없습니다.")
		@Schema(description = "이미지 경로", example = "https://~.jpeg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
		String coverImage,

		@Length(min = 0, max = 10, message = "역할은 {min}자 이상 {max}자 이하만 가능합니다.")
		@Schema(description = "역할", example = "총무", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
		String role

) {
}
