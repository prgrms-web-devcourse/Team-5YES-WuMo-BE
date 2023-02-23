package org.prgrms.wumo.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(name = "비공개 루트 댓글 수정 요청 정보")
public record PrivateRouteCommentUpdateRequest(

		@NotNull(message = "수정하고자 하는 댓글의 id는 필수 입력값입니다")
		@Schema(description = "수정하는 비공개 댓글", example = "1", required = true)
		Long id,

		@NotEmpty(message = "댓글의 내용은 공백일 수 없습니다.")
		@Schema(description = "댓글 수정 내용", example = "변경된 내용", required = true)
		String content
) {
}
