package org.prgrms.wumo.domain.member.dto.request;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "닉네임 중복체크 요청 정보")
public record MemberNicknameCheckRequest(
	@NotBlank(message = "닉네임은 필수 입력사항입니다.")
	@Schema(description = "중복 체크할 회원 닉네임", example = "오예스", requiredMode = Schema.RequiredMode.REQUIRED)
	String nickname
) {
}
