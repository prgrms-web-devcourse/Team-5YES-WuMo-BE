package org.prgrms.wumo.domain.route.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "(루트 목록에서)루트의 후보지 응답 정보")
public record RouteLocationSimpleResponse(
	@Schema(description = "후보지 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	Long id,

	@Schema(description = "후보지 상호명", example = "오예스 식당", requiredMode = Schema.RequiredMode.REQUIRED)
	String name,

	@Schema(description = "후보지 주소", example = "부산광역시 수영구 ~~~ ", requiredMode = Schema.RequiredMode.REQUIRED)
	String address,

	@Schema(description = "후보지 이미지", example = "https://~", requiredMode = Schema.RequiredMode.REQUIRED)
	String image
) {
}
