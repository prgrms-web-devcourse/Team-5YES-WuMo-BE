package org.prgrms.wumo.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import org.prgrms.wumo.domain.comment.model.ContentType;

@Schema(name = "후보지 댓글 전체 조회 결과 정보")
public record LocationCommentGetAllResponse(

		@Schema(description = "댓글 id")
		Long id,

		@Schema(description = "댓글 등록인 닉네임")
		String nickName,

		@Schema(description = "댓글 등록인 프로필 사진")
		String profileImage,

		@Schema(description = "댓글 등록인 역할")
		String memberRole,

		@Schema(description = "댓글 내용")
		String content,

		@Schema(description = "댓글 내용 타입")
		ContentType contentType,

		@Schema(description = "댓글 등록 시간")
		LocalDateTime createdAt
) {
}
