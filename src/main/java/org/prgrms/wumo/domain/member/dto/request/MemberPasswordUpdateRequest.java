package org.prgrms.wumo.domain.member.dto.request;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "비밀번호 변경 요청 정보")
public record MemberPasswordUpdateRequest(
		@NotBlank(message = "비밀번호는 필수 입력사항입니다.")
		@Schema(description = "기존 비밀번호", example = "qwe12345", requiredMode = Schema.RequiredMode.REQUIRED)
		String password,

		@NotBlank(message = "비밀번호는 필수 입력사항입니다.")
		@Schema(description = "변경 비밀번호", example = "asd45678", requiredMode = Schema.RequiredMode.REQUIRED)
		String newPassword
) {
}
