package org.prgrms.wumo.domain.member.dto.request;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "회원 로그인 요청 정보")
public record MemberLoginRequest(
	@NotBlank(message = "이메일은 필수 입력사항입니다.")
	@Schema(description = "회원 이메일", example = "5yes@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
	String email,

	@NotBlank(message = "비밀번호는 필수 입력사항입니다.")
	@Schema(description = "회원 비밀번호", example = "!5yes1234", requiredMode = Schema.RequiredMode.REQUIRED)
	String password
) {
}
