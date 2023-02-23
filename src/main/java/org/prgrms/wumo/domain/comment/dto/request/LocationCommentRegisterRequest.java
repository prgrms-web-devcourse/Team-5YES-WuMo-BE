package org.prgrms.wumo.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

@Schema(name = "후보지 댓글 생성 요청 정보")
public record LocationCommentRegisterRequest(

		@NotNull(message = "댓글을 다는 회원의 id는 필수 입력값입니다.")
		@Schema(description = "댓글 작성자 id", example = "1", required = true)
		Long memberId,

		@Schema(description = "댓글 내용", example = "댓글 내용", required = false)
		String content,

		@Schema(description = "댓글 첨부 사진 주소", example = "http://", required = false)
		String image,

		@NotNull(message = "댓글을 다는 후보지의 정보는 필수 입력값입니다.")
		@Schema(description = "댓글이 작성된 후보지 id", example = "1", required = true)
		Long locationId
) {
	public LocationCommentRegisterRequest(
			Long memberId, String content, String image, Long locationId
	){
		if (content.isEmpty() && image.isEmpty())
			throw new ValidationException("");

		this.memberId = memberId;
		this.content = content;
		this.image = image;
		this.locationId = locationId;
	}
}
