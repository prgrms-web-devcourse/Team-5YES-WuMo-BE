package org.prgrms.wumo.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

@Schema(name = "모임 내 루트 댓글 생성 요청")
public record PartyRouteCommentRegisterRequest(

		@Schema(description = "댓글 내용", example = "댓글", requiredMode = Schema.RequiredMode.REQUIRED)
		String content,

		@Schema(description = "댓글 이미지 주소", example = "http://~.png", requiredMode = Schema.RequiredMode.REQUIRED)
		String image,

		@NotNull(message = "댓글이 쓰여지는 모임의 식별자는 필수 입력값입니다.")
		@Schema(description = "모임 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long partyId,

		@NotNull(message = "댓글이 쓰여지는 후보지의 식별자는 필수 입력값입니다. ")
		@Schema(description = "후보지 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long locationId
) {
	public PartyRouteCommentRegisterRequest(
			String content, String image, Long partyId, Long locationId
	) {
		if (content.isEmpty() && image.isEmpty()) {
			throw new ValidationException("댓글에 이미지나 내용 둘 중 하나는 있어야 합니다.");
		}

		this.content = content;
		this.image = image;
		this.partyId = partyId;
		this.locationId = locationId;
	}
}
