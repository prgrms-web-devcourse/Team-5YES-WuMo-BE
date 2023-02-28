package org.prgrms.wumo.domain.route.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record RouteLocationSimpleResponse(
	@Schema(description = "후보장소 식별자", example = "1", required = true)
	Long id,

	@Schema(description = "후보장소 상호명", example = "오예스 식당", required = true)
	String name,

	@Schema(description = "후보장소 주소", example = "부산광역시 수영구 ~~~ ", required = true)
	String address,

	@Schema(description = "후보장소 이미지", example = "https://~", required = true)
	String image
) {
}
