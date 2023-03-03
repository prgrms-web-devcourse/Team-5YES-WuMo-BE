package org.prgrms.wumo.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "내 정보 조회 응답 정보")
public record MemberGetResponse(
	@Schema(description = "회원 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	Long id,

	@Schema(description = "회원 이메일", example = "5yes@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
	String email,

	@Schema(description = "회원 닉네임", example = "오예스", requiredMode = Schema.RequiredMode.REQUIRED)
	String nickname,

	@Schema(description = "회원 프로필 이미지", example = "https://~", requiredMode = Schema.RequiredMode.REQUIRED)
	String profileImage
) {
}
