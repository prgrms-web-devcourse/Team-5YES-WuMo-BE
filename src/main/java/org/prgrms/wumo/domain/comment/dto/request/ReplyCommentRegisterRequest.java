package org.prgrms.wumo.domain.comment.dto.request;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "대댓글 등록 요청 정보")
public record ReplyCommentRegisterRequest(
		@NotNull(message = "대댓글을 달려고 하는 댓글의 식별자는 필수 입력값이입니다.")
		@Schema(description = "대댓글을 다는 댓글 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long commentId,

		@NotNull(message = "대댓글은 내용을 필수로 입력해야합니다.")
		@Schema(description = "대댓글 내용", example = "좋아요~~", requiredMode = Schema.RequiredMode.REQUIRED)
		String content
) {
}
