package org.prgrms.wumo.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(name = "후보지 댓글 상세 응답 정보")
public record LocationCommentGetResponse(

		@Schema(description = "댓글 id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long id,

		@Schema(description = "댓글 등록인 닉네임", example = "보섭", requiredMode = Schema.RequiredMode.REQUIRED)
		String nickName,

		@Schema(description = "댓글 등록인 프로필 사진", example = "http://~.png", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
		String profileImage,

		@Schema(description = "댓글 등록인 역할", example = "총무", requiredMode = Schema.RequiredMode.REQUIRED)
		String memberRole,

		@Schema(description = "댓글 내용", example = "이거 먹으러 가자!", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
		String content,

		@Schema(description = "댓글 첨부 이미지", example = "http://~.jpeg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
		String image,

		@Schema(description = "댓글 등록 시간", example = "2023-03-03T13:03:23", requiredMode = Schema.RequiredMode.REQUIRED)
		LocalDateTime createdAt,

		@Schema(description = "댓글 수정 시간", example = "2023-03-03T14:03:23", requiredMode = Schema.RequiredMode.REQUIRED)
		LocalDateTime updatedAt
) {
}
