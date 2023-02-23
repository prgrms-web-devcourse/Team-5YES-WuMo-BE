package org.prgrms.wumo.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import org.prgrms.wumo.domain.comment.model.ContentType;

@Schema(name = "비공개 루트 댓글 생성 요청")
public record PrivateRouteCommentRegisterRequest(

		@NotBlank(message = "댓글 성성 요청자의 id는 필수 입력값입니다")
		@Schema(description = "댓글 생성 요청자 id", example = "1", required = true)
		Long memberId,

		@NotEmpty(message = "댓글의 내용은 공백일 수 없습니다.")
		@Schema(description = "댓글 내용", example = "댓글", required = true)
		String content,

		@NotBlank(message = "댓글 내용의 타입은 필수 입력값입니다.")
		@Schema(description = "댓글 내용 형식", example = "TEXT", required = true)
		ContentType contentType,

		@NotBlank(message = "댓글이 쓰여지는 경로의 id는 필수 입력값입니다.")
		@Schema(description = "루트 id", example = "1", required = true)
		Long routeId
) {
}
