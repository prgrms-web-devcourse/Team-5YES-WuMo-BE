package org.prgrms.wumo.domain.route.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "루트 상세 조회 요청 정보")
public record RouteGetRequest(
	@Schema(description = "0이라면 내 모임에서 접근, 1이라면 공개된 루트목록에서 접근", example = "1", required = true)
	int isPublic
) {
}
