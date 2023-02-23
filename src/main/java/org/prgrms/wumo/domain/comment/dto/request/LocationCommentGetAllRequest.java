package org.prgrms.wumo.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Schema(name = "후보지 댓글 전체 조회 요청 정보")
public record LocationCommentGetAllRequest(

		@Schema(description = "커서 아이디", example = "0", required = false)
		Long cursorId,

		@NotBlank(message = "페이지 사이즈는 필수 입력값입니다.")
		@Positive(message = "페이지 사이즈는 양수여야합니다.")
		@Schema(description = "페이지 사이즈", example = "5", required = true)
		int pageSize,

		@NotBlank(message = "조회할 후보지 id는 필수 입력값입니다.")
		@Schema(description = "댓글을 조회할 후보지", example = "1", required = true)
		Long locationId
) {
}
