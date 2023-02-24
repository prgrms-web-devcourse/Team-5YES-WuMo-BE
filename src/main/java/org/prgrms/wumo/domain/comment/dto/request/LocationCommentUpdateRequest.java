package org.prgrms.wumo.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

@Schema(name = "후보지 댓글 수정 요청 정보")
public record LocationCommentUpdateRequest(

		@NotNull(message = "수정하고자 하는 댓글의 id는 필수 입력값입니다")
		@Schema(description = "수정하는 후보지 댓글", example = "1", required = true)
		Long id,

		@Schema(description = "댓글 수정 내용", example = "변경된 내용", required = true)
		String content,

		@Schema(description = "댓글 이미지 수정", example = "http://", required = true)
		String image
) {
	public LocationCommentUpdateRequest(
			Long id, String content, String image
	){
		if (content.isEmpty() && image.isEmpty())
			throw new ValidationException("");

		this.id = id;
		this.content = content;
		this.image = image;
	}
}