package org.prgrms.wumo.domain.comment.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "대댓글 목록 조회 응답 정보")
public record ReplyCommentGetAllResponse(
		@Schema(description = "대댓글 목록 정보")
		List<ReplyCommentGetResponse> replyComments,

		@Schema(description = "커서 식별자", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
		long lastId
) {
}
