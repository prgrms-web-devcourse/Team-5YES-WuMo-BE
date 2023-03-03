package org.prgrms.wumo.domain.route.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "루트 목록 조회 요청 정보")
public record RouteGetAllRequest(
	@Schema(description = "커서 식별자", example = "9", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	Long cursorId,

	@NotNull
	@Positive(message = "page size는 0 또는 음수일 수 없습니다.")
	@Schema(description = "페이지 사이즈", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
	int pageSize,

	@Schema(description = "정렬 기준(0이면(default) 최신순, 1이면 좋아요순)", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	int sortType,

	@Schema(description = "검색어(지역)", example = "부산광역시", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	String searchWord
) {
}
