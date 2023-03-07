package org.prgrms.wumo.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

@Schema(name = "후보지 댓글 생성 요청 정보")
public record LocationCommentRegisterRequest(

		@Schema(description = "댓글 내용", example = "댓글 내용", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
		String content,

		@Schema(description = "댓글 이미지 주소", example = "http://~.png", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
		String image,

		@NotNull(message = "댓글을 다는 후보지의 식별자는 필수 입력값입니다.")
		@Schema(description = "댓글이 작성된 후보지 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long locationId,

		@NotNull(message = "모임 식별자는 필수 입력값입니다.")
		@Schema(description = "댓글을 다는 모임 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long partyId

) {
	public LocationCommentRegisterRequest(
			String content, String image, Long locationId, Long partyId
	) {
		if (content.isEmpty() && image.isEmpty())
			throw new ValidationException("댓글 내용 또는 이미지 둘 중 하나는 입력해야합니다.");

		this.content = content;
		this.image = image;
		this.locationId = locationId;
		this.partyId = partyId;
	}
}
