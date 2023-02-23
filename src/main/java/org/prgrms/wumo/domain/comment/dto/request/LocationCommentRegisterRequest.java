package org.prgrms.wumo.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import org.prgrms.wumo.domain.comment.model.ContentType;

@Schema(name = "후보지 댓글 생성 요청 정보")
public record LocationCommentRegisterRequest(

		@NotBlank(message = "댓글을 다는 회원의 id는 필수 입력값입니다.")
		@Schema(description = "댓글 작성자 id", example = "1", required = true)
		Long memberId,

		@NotEmpty(message = "댓글의 내용은 공백일 수 없습니다.")
		@Schema(description = "댓글 내용", example = "댓글 내용", required = true)
		String content,

		@NotBlank(message = "댓글의 내용 타입은 필수 입력값입니다.")
		@Schema(description = "댓글 내용 타입", example = "TEXT", required = true)
		ContentType contentType,

		@NotBlank(message = "댓글을 다는 후보지의 정보는 필수 입력값입니다.")
		@Schema(description = "댓글이 작성된 후보지 id", example = "1", required = true)
		Long locationId
) {
}
