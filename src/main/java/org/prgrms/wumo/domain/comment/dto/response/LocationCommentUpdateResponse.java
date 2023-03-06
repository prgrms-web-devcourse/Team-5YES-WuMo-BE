package org.prgrms.wumo.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "후보지 댓글 수정 응답 정보")
public record LocationCommentUpdateResponse(

		@Schema(description = "수정된 후보지 댓글 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long id,

		@Schema(description = "수정된 후보지 댓글 내용", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
		String content,

		@Schema(description = "수정된 후보지 댓글 이미지 주소", example = "http://~.png", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
		String image
) {
}
