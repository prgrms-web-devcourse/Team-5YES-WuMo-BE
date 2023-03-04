package org.prgrms.wumo.domain.route.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "루트 공개여부 변경 요청 정보")
public record RouteStatusUpdateRequest(
	@NotNull(message = "공개할 루트를 선택해주세요.")
	@Schema(description = "공개할 루트 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	Long routeId,

	@Schema(description = "루트 공개여부(true면 공개)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	boolean isPublic,

	@NotBlank(message = "루트 이름은 필수 입력사항입니다.")
	@Schema(description = "루트 공개 시 보여줄 루트 이름", example = "퇴사 기념 여행", requiredMode = Schema.RequiredMode.REQUIRED)
	String name
) {
}
