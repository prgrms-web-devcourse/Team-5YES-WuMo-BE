package org.prgrms.wumo.domain.member.dto.request;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "이메일 인증 코드 검증 요청 정보")
public record MemberCodeCheckRequest(
	@NotBlank(message = "이메일 주소는 필수 사항입니다.")
	@Schema(description = "인증 검증을 요청하는 회원 이메일", example = "5yes@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
	String address,

	@NotBlank(message = "인증 코드는 필수 사항입니다.")
	@Schema(description = "인증 코드(6자리 숫자)", example = "453627", requiredMode = Schema.RequiredMode.REQUIRED)
	String code
) {
}
