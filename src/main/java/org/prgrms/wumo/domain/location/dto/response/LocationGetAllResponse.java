package org.prgrms.wumo.domain.location.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "후보장소 목록 조회 응답 정보")
public record LocationGetAllResponse(
		@Schema(description = "모임의 후보장소들", required = true)
		List<LocationGetResponse> locations,

		@Schema(description = "마지막으로 조회 한 후보 장소 식별자", required = true)
		Long lastId
) {
}
