package org.prgrms.wumo.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "모임 내 루트 댓글 전체 응답 정보")
public record PartyRouteCommentGetAllResponse(
		@Schema(description = "모임 내 루트 댓글 목록 정보")
		List<PartyRouteCommentGetResponse> partyRouteComments,

		@Schema(description = "커서 아이디", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
		long lastId
) {
}
