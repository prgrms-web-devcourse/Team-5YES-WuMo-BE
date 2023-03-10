package org.prgrms.wumo.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "모임 내 루트 댓글 생성 응답 정보")
public record PartyRouteCommentRegisterResponse(

		@Schema(description = "모임 내 루트에 작성된 댓글 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long id
) {
}
