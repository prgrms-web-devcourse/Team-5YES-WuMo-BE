package org.prgrms.wumo.domain.member.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "내 정보 수정 요청 정보")
public record MemberUpdateRequest(
	@NotNull
	@Schema(description = "수정할 회원 아이디", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	Long id,

	@NotBlank(message = "닉네임은 필수 입력사항입니다.")
	@Schema(description = "회원 닉네임 수정 내용", example = "오예스딸기", requiredMode = Schema.RequiredMode.REQUIRED)
	String nickname,

	@Schema(description = "회원 프로필 이미지 수정 내용", example = "https://~", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	String profileImage
) {
}
