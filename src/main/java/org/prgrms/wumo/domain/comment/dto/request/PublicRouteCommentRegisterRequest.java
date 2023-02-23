package org.prgrms.wumo.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

@Schema(name = "공개 루트 댓글 생성 요청")
public record PublicRouteCommentRegisterRequest(

		@NotNull(message = "댓글 성성 요청자의 id는 필수 입력값입니다")
		@Schema(description = "댓글 생성 요청자 id", example = "1", required = true)
		Long memberId,

		@Schema(description = "댓글 내용", example = "댓글", required = true)
		String content,

		@Schema(description = "댓글 첨부 사진 링크", example = "http://", required = true)
		String image,

		@NotNull(message = "댓글이 쓰여지는 경로의 id는 필수 입력값입니다.")
		@Schema(description = "루트 id", example = "1", required = true)
		Long routeId
) {
	public PublicRouteCommentRegisterRequest(
			Long memberId, String content, String image, Long routeId
	){
		if (content.isEmpty() && image.isEmpty())
			throw new ValidationException("");
		this.memberId = memberId;
		this.content = content;
		this.image = image;
		this.routeId = routeId;
	}
}
