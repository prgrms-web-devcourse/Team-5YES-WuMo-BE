package org.prgrms.wumo.domain.comment.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "대댓글 등록 요청 정보")
public record ReplyCommentRegisterRequest(

		@NotNull(message = "대댓글을 다는 댓글의 식별자는 필수 입력값입니다.")
		@Schema(description = "대댓글이 작성된 댓글 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long commentId,

		@NotBlank(message = "대댓글은 내용이 빌 수 없습니다.")
		@Schema(description = "대댓글 내용", example = "댓글 내용", requiredMode = Schema.RequiredMode.REQUIRED)
		String content
) {
}
