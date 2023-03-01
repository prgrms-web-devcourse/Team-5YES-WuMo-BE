package org.prgrms.wumo.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

@Schema(name = "모임 내 일정 댓글 생성 요청")
public record PartyRouteCommentRegisterRequest(

		@NotNull(message = "댓글 성성 요청자의 id는 필수 입력값입니다")
		@Schema(description = "댓글 생성 요청자 id", example = "1", required = true)
		Long memberId,

		@Schema(description = "댓글 내용", example = "댓글", required = true)
		String content,

		@Schema(description = "댓글 사진 링크", example = "http://", required = true)
		String image,

		@NotNull(message = "댓글이 쓰여지는 일정의 id는 필수 입력값입니다.")
		@Schema(description = "루트 id", example = "1", required = true)
		Long routeId,

		@NotNull(message = "댓글이 쓰여지는 일정에서의 장소 id는 필수 입력값입니다. ")
		@Schema(description = "", example = "1", required = true)
		Long locationId
) {
	public PartyRouteCommentRegisterRequest(
			Long memberId, String content, String image, Long routeId, Long locationId
	) {
		if (content.isEmpty() && image.isEmpty()) {
			throw new ValidationException("댓글에 이미지나 내용 둘 중 하나는 있어야 합니다.");
		}

		this.memberId = memberId;
		this.content = content;
		this.image = image;
		this.routeId = routeId;
		this.locationId = locationId;
	}
}
