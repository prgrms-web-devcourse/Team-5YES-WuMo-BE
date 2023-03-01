package org.prgrms.wumo.domain.route.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "루트 목록 조회 요청 정보")
public record RouteGetAllRequest(
	@Schema(description = "커서 식별자", example = "9", required = false)
	Long cursorId,

	@NotNull
	@Positive(message = "page size는 0 또는 음수일 수 없습니다.")
	@Schema(description = "페이지 사이즈", example = "5", required = true)
	int pageSize

	//기본 : 최신순, 좋아요순인지, 지난달 베스트 일정인지
	// @Schema(description = "정렬 기준", required = false, example = "기본 : 최신순// 좋아요순인지, 지난달 베스트 일정인지 등등 어떻게 받을지 상의필요")
	// String sortType,

	//조건들(미정)
	// @Schema(description = "검색어", required = false, example = "부산(미정///지역별, 상호명별 등등 나누게 될듯함,,)")
	// String searchWord
) {
}
