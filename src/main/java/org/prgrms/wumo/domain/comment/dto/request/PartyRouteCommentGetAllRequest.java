package org.prgrms.wumo.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Schema(name = "모임 내 루트 댓글 전체 조회 요청 정보")
public record PartyRouteCommentGetAllRequest(

		@Schema(description = "커서 아이디", example = "0", required = false)
		Long cursorId,

		@NotNull(message = "페이지 사이즈는 필수 입력값입니다.")
		@Positive(message = "페이지 사이즈는 양수여야합니다.")
		@Schema(description = "페이지 사이즈", example = "5", required = true)
		int pageSize,

		@NotNull(message = "모임 내 일정에서 댓글을 조회할 후보지의 식별자는 필수 입력값입니다.")
		@Schema(description = "댓글을 조회할 모임 내 루트에서의 후보지 식별자", example = "1", required = true)
		Long locationId
) {
}
