package org.prgrms.wumo.domain.member.dto.request;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "회원가입 요청 정보")
public record MemberRegisterRequest(
	@NotBlank(message = "이메일은 필수 입력사항입니다.")
	@Schema(description = "회원 이메일", example = "5yes@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
	String email,

	@NotBlank(message = "닉네임은 필수 입력사항입니다.")
	@Schema(description = "회원 닉네임", example = "오예스", requiredMode = Schema.RequiredMode.REQUIRED)
	String nickname,

	@NotBlank(message = "비밀번호는 필수 입력사항입니다.")
	@Schema(description = "회원 비밀번호(소문자와 숫자 둘 다 포함 8글자 이상, 특수문자 불가)", example = "5yes1234", requiredMode = Schema.RequiredMode.REQUIRED)
	String password
) {
}
