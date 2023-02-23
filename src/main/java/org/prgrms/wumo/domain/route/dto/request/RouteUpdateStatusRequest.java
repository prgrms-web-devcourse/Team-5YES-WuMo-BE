package org.prgrms.wumo.domain.route.dto.request;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "루트 공개여부 변경 요청 정보")
public record RouteUpdateStatusRequest(
	@NotNull(message = "공개할 루트를 선택해주세요.")
	@Schema(description = "공개할 루트 식별자", required = true, example = "1")
	Long routeId,

	@Schema(description = "루트 공개여부(true면 공개)", required = true, example = "true")
	boolean isPublic
) {
}
