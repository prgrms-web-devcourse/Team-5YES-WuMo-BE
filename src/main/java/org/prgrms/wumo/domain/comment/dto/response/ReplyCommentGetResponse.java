package org.prgrms.wumo.domain.comment.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "대댓글 조회 응답 정보")
public record ReplyCommentGetResponse(
		@Schema(description = "대댓글 식별자", requiredMode = Schema.RequiredMode.REQUIRED)
		Long id,

		@Schema(description = "대댓글 등록인 닉네임", example = "보섭", requiredMode = Schema.RequiredMode.REQUIRED)
		String nickName,

		@Schema(description = "대댓글 등록인 프로필 사진", example = "http://~.png", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
		String profileImage,

		@Schema(description = "대댓글 내용", example = "이거 먹으러 가자!", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
		String content,

		@Schema(description = "대댓글 등록 시간", example = "2023-03-03T13:03:23", requiredMode = Schema.RequiredMode.REQUIRED)
		LocalDateTime createdAt,

		@Schema(description = "대댓글 수정 시간", example = "2023-03-03T14:03:23", requiredMode = Schema.RequiredMode.REQUIRED)
		LocalDateTime updatedAt,

		@Schema(description = "대댓글 수정 및 삭제 가능 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
		boolean isEditable
) {
}
