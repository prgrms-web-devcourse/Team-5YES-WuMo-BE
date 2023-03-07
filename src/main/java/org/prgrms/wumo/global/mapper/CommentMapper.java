package org.prgrms.wumo.global.mapper;

import java.util.List;

import org.prgrms.wumo.domain.comment.dto.request.LocationCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentGetAllResponse;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentGetResponse;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentUpdateResponse;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentGetAllResponse;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentGetResponse;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentUpdateResponse;
import org.prgrms.wumo.domain.comment.model.LocationComment;
import org.prgrms.wumo.domain.comment.model.PartyRouteComment;
import org.prgrms.wumo.domain.party.model.PartyMember;

public class CommentMapper {
	public static LocationCommentRegisterResponse toLocationCommentRegisterResponse(LocationComment locationcomment) {
		return new LocationCommentRegisterResponse(
				locationcomment.getId()
		);
	}

	public static LocationComment toLocationComment(LocationCommentRegisterRequest locationCommentRegisterRequest, PartyMember partyMember) {
		return LocationComment.builder()
				.locationId(locationCommentRegisterRequest.locationId())
				.content(locationCommentRegisterRequest.content())
				.image(locationCommentRegisterRequest.image())
				.partyMember(partyMember)
				.build();
	}

	public static LocationCommentGetResponse toLocationCommentGetResponse(LocationComment locationComment) {
		return new LocationCommentGetResponse(
				locationComment.getId(),
				locationComment.getMember().getNickname(),
				locationComment.getMember().getProfileImage(),
				locationComment.getPartyMember().getRole(),
				locationComment.getContent(),
				locationComment.getImage(),
				locationComment.getCreatedAt(),
				locationComment.getUpdatedAt()
		);
	}

	public static LocationCommentGetAllResponse toLocationCommentGetAllResponse(
			List<LocationComment> locationComments, Long lastId
	) {
		return new LocationCommentGetAllResponse(
				locationComments.stream().map(CommentMapper::toLocationCommentGetResponse).toList(),
				lastId
		);
	}

	public static LocationCommentUpdateResponse toLocationCommentUpdateResponse(LocationComment locationComment) {
		return new LocationCommentUpdateResponse(
				locationComment.getId(),
				locationComment.getContent(),
				locationComment.getImage()
		);
	}

	public static PartyRouteCommentRegisterResponse toPartyRouteCommentRegisterResponse(
			PartyRouteComment partyRouteComment) {
		return new PartyRouteCommentRegisterResponse(
				partyRouteComment.getId()
		);
	}

	public static PartyRouteComment toPartyRouteComment(
			PartyRouteCommentRegisterRequest partyRouteCommentRegisterRequest, Long routeId) {
		return PartyRouteComment.builder()
				.locationId(partyRouteCommentRegisterRequest.locationId())
				.routeId(routeId)
				.content(partyRouteCommentRegisterRequest.content())
				.image(partyRouteCommentRegisterRequest.image())
				.build();
	}

	public static PartyRouteCommentGetResponse toPartyRouteCommentGetResponse(PartyRouteComment partyRouteComment) {
		return new PartyRouteCommentGetResponse(
				partyRouteComment.getId(),
				partyRouteComment.getMember().getNickname(),
				partyRouteComment.getMember().getProfileImage(),
				partyRouteComment.getPartyMember().getRole(),
				partyRouteComment.getContent(),
				partyRouteComment.getImage(),
				partyRouteComment.getCreatedAt(),
				partyRouteComment.getUpdatedAt()
		);
	}

	public static PartyRouteCommentGetAllResponse toPartyRouteCommentGetAllResponse(
			List<PartyRouteComment> partyRouteComments, long lastId) {
		return new PartyRouteCommentGetAllResponse(
				partyRouteComments.stream().map(CommentMapper::toPartyRouteCommentGetResponse).toList(),
				lastId
		);
	}

	public static PartyRouteCommentUpdateResponse toPartyRouteCommentUpdateResponse(PartyRouteComment partyRouteComment) {
		return new PartyRouteCommentUpdateResponse(
				partyRouteComment.getId(),
				partyRouteComment.getContent(),
				partyRouteComment.getImage()
		);
	}
}
