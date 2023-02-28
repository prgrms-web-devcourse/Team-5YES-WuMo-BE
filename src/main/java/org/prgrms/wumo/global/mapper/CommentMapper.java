package org.prgrms.wumo.global.mapper;

import org.prgrms.wumo.domain.comment.dto.request.LocationCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.model.LocationComment;

public class CommentMapper {
	public static LocationCommentRegisterResponse toLocationCommentRegisterResponse(LocationComment locationcomment){
		return new LocationCommentRegisterResponse(
				locationcomment.getId()
		);
	}

	public static LocationComment toLocationComment(LocationCommentRegisterRequest locationCommentRegisterRequest){
		return LocationComment.builder()
				.locationId(locationCommentRegisterRequest.locationId())
				.content(locationCommentRegisterRequest.content())
				.image(locationCommentRegisterRequest.image())
				.locationId(locationCommentRegisterRequest.locationId())
				.build();
	}
}
