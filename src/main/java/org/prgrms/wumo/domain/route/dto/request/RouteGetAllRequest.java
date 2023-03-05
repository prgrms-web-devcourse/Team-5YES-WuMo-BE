package org.prgrms.wumo.domain.route.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "루트 목록 조회 요청 정보")
public record RouteGetAllRequest(
	@Schema(description = "커서 식별자", example = "9", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	Long cursorId,

	@NotNull(message = "페이지 사이즈는 필수 입력 사항입니다.")
	@Positive(message = "페이지 사이즈는 0 또는 음수일 수 없습니다.")
	@Schema(description = "페이지 사이즈", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
	int pageSize,

	@NotNull(message = "정렬 기준은 필수 선택 사항입니다.")
	@Schema(description = "정렬 기준(NEWEST, LIKES)", example = "NEWEST", requiredMode = Schema.RequiredMode.REQUIRED)
	SortType sortType,

	@Schema(description = "검색어(지역)", example = "부산광역시", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	String searchWord
) {
}
