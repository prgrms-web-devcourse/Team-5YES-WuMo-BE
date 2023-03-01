package org.prgrms.wumo.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(name = "모임 내 루트 댓글 응답 정보")
public record PartyRouteCommentGetResponse(

		@Schema(description = "댓글 id")
		Long id,

		@Schema(description = "댓글 등록인 닉네임")
		String nickName,

		@Schema(description = "댓글 등록인 프로필 사진")
		String profileImage,

		@Schema(description = "후보지 댓글 등록인 역할")
		String memberRole,

		@Schema(description = "댓글 내용")
		String content,

		@Schema(description = "댓글 첨부 사진")
		String image,

		@Schema(description = "댓글 등록 시간")
		LocalDateTime createdAt,

		@Schema(description = "댓글 수정 시간")
		LocalDateTime updatedAt
) {
}
