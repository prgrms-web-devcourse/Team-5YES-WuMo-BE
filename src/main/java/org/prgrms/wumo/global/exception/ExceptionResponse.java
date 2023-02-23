package org.prgrms.wumo.global.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "예외 발생 시 응답 정보")
public record ExceptionResponse(
	@Schema(description = "예외 메시지", example = "이메일이 중복됩니다", required = true)
	String message
) {
}
