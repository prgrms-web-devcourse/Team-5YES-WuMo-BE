package org.prgrms.wumo.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "내 정보 조회 응답 정보")
public record MemberGetResponse(
		@Schema(description = "회원 식별자", example = "1", required = true)
		Long id,

		@Schema(description = "회원 이메일", example = "5yes@gmail.com", required = true)
		String email,

		@Schema(description = "회원 닉네임", example = "오예스", required = true)
		String nickname,

		@Schema(description = "회원 비밀번호", example = "!5yes1234", required = true)
		String password,

		@Schema(description = "회원 프로필 이미지", example = "https://~", required = true)
		String profileImage
) {
}
