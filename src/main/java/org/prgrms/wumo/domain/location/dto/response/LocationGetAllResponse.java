package org.prgrms.wumo.domain.location.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "후보지 목록 조회 응답 정보")
public record LocationGetAllResponse(
		@Schema(description = "모임의 후보지 목록", requiredMode = Schema.RequiredMode.REQUIRED)
		List<LocationGetResponse> locations,

		@Schema(description = "마지막으로 조회한 후보지 식별자", requiredMode = Schema.RequiredMode.REQUIRED)
		Long lastId
) {
}
