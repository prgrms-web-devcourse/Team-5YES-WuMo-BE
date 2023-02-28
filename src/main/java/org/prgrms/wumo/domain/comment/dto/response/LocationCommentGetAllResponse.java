package org.prgrms.wumo.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "특정 후보지 내의 전체 댓글 응답 정보")
public record LocationCommentGetAllResponse(

		@Schema(description = "후보지 댓글 목록 정보")
		List<LocationCommentGetResponse> locationComments,

		@Schema(description = "커서 아이디", example = "10", required = true)
		Long lastId
) {
}
