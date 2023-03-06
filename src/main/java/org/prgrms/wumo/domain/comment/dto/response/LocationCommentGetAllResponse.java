package org.prgrms.wumo.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "후보지 댓글 전체 조회 응답 정보")
public record LocationCommentGetAllResponse(

		@Schema(description = "후보지 댓글 목록 정보")
		List<LocationCommentGetResponse> locationComments,

		@Schema(description = "커서 식별자", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
		Long lastId
) {
}
