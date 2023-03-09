package org.prgrms.wumo.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "대댓글 등록 응답 정보")
public record ReplyCommentRegisterResponse(
		@Schema(description = "등록된 대댓글 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long id
) {
}
