package org.prgrms.wumo.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "후보지 댓글 생성 응답 정보")
public record LocationCommentRegisterResponse (

		@Schema(description = "생성된 후보지 댓글 id")
		Long id
){
}
