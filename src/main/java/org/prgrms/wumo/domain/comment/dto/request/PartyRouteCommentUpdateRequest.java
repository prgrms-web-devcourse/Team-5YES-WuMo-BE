package org.prgrms.wumo.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

@Schema(name = "모임 내 루트 댓글 수정 요청 정보")
public record PartyRouteCommentUpdateRequest(

		@NotNull(message = "수정하고자 하는 댓글의 식별자는 필수 입력값입니다")
		@Schema(description = "모임 내 루트 댓글 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long id,

		@Schema(description = "수정된 댓글 내용", example = "수정된 내용", requiredMode = Schema.RequiredMode.REQUIRED)
		String content,

		@Schema(description = "수정된 댓글 이미지 주소", example = "http://~.png", requiredMode = Schema.RequiredMode.REQUIRED)
		String image
) {
	public PartyRouteCommentUpdateRequest(
			Long id, String content, String image
	){
		if (content.isEmpty() && image.isEmpty())
			throw new ValidationException("내용이 빌 수는 없습니다.");

		this.id = id;
		this.content = content;
		this.image = image;
	}
}
