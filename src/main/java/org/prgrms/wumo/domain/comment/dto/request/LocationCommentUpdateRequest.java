package org.prgrms.wumo.domain.comment.dto.request;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "후보지 댓글 수정 요청 정보")
public record LocationCommentUpdateRequest(

		@NotNull(message = "수정하고자 하는 댓글의 식별자는 필수 입력값입니다")
		@Schema(description = "수정하는 후보지 댓글 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long id,

		@Schema(description = "수정된 댓글 내용", example = "변경된 내용", requiredMode = Schema.RequiredMode.REQUIRED)
		String content,

		@Schema(description = "수정된 댓글 이미지", example = "http://", requiredMode = Schema.RequiredMode.REQUIRED)
		String image
) {
	public LocationCommentUpdateRequest(
			Long id, String content, String image
	) {
		if (content.isEmpty() && image.isEmpty())
			throw new ValidationException("");

		this.id = id;
		this.content = content;
		this.image = image;
	}
}
