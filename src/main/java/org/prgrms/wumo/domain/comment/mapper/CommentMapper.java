package org.prgrms.wumo.domain.comment.mapper;

import java.util.ArrayList;
import java.util.List;

import org.prgrms.wumo.domain.comment.dto.request.LocationCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.request.ReplyCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentGetAllResponse;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentGetResponse;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.dto.response.LocationCommentUpdateResponse;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentGetAllResponse;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentGetResponse;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.dto.response.PartyRouteCommentUpdateResponse;
import org.prgrms.wumo.domain.comment.dto.response.ReplyCommentGetAllResponse;
import org.prgrms.wumo.domain.comment.dto.response.ReplyCommentGetResponse;
import org.prgrms.wumo.domain.comment.dto.response.ReplyCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.model.LocationComment;
import org.prgrms.wumo.domain.comment.model.PartyRouteComment;
import org.prgrms.wumo.domain.comment.model.ReplyComment;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.party.model.PartyMember;

public class CommentMapper {
	public static LocationCommentRegisterResponse toLocationCommentRegisterResponse(LocationComment locationcomment) {
		return new LocationCommentRegisterResponse(
				locationcomment.getId()
		);
	}

	public static LocationComment toLocationComment(LocationCommentRegisterRequest locationCommentRegisterRequest,
			PartyMember partyMember) {
		return LocationComment.builder()
				.locationId(locationCommentRegisterRequest.locationId())
				.content(locationCommentRegisterRequest.content())
				.image(locationCommentRegisterRequest.image())
				.partyMember(partyMember)
				.build();
	}

	public static LocationCommentGetResponse toLocationCommentGetResponse(LocationComment locationComment,
			boolean isEditable) {
		return new LocationCommentGetResponse(
				locationComment.getId(),
				locationComment.getMember().getNickname(),
				locationComment.getMember().getProfileImage(),
				locationComment.getPartyMember().getRole(),
				locationComment.getContent(),
				locationComment.getImage(),
				locationComment.getCreatedAt(),
				locationComment.getUpdatedAt(),
				isEditable
		);
	}

	public static LocationCommentGetAllResponse toLocationCommentGetAllResponse(
			List<LocationComment> locationComments, List<Boolean> isEditables, Long lastId
	) {

		List<LocationCommentGetResponse> locationCommentGetResponses = new ArrayList<>();
		for (int i = 0; i < locationComments.size(); i++) {
			locationCommentGetResponses.add(toLocationCommentGetResponse(locationComments.get(i), isEditables.get(i)));
		}

		return new LocationCommentGetAllResponse(
				locationCommentGetResponses,
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

	public static PartyRouteCommentGetResponse toPartyRouteCommentGetResponse(PartyRouteComment partyRouteComment,
			boolean isEditable) {
		return new PartyRouteCommentGetResponse(
				partyRouteComment.getId(),
				partyRouteComment.getMember().getNickname(),
				partyRouteComment.getMember().getProfileImage(),
				partyRouteComment.getPartyMember().getRole(),
				partyRouteComment.getContent(),
				partyRouteComment.getImage(),
				partyRouteComment.getCreatedAt(),
				partyRouteComment.getUpdatedAt(),
				isEditable
		);
	}

	public static PartyRouteCommentGetAllResponse toPartyRouteCommentGetAllResponse(
			List<PartyRouteComment> partyRouteComments, List<Boolean> isEditables, long lastId) {
		List<PartyRouteCommentGetResponse> partyRouteCommentGetResponses = new ArrayList<>();
		for (int i = 0; i < partyRouteComments.size(); i++) {
			partyRouteCommentGetResponses.add(toPartyRouteCommentGetResponse(partyRouteComments.get(i), isEditables.get(i)));
		}
		return new PartyRouteCommentGetAllResponse(
				partyRouteCommentGetResponses,
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

	public static ReplyComment toReplyComment(ReplyCommentRegisterRequest replyCommentRegisterRequest, Member member){
		return ReplyComment.builder()
				.member(member)
				.commentId(replyCommentRegisterRequest.commentId())
				.content(replyCommentRegisterRequest.content())
				.build();
	}

	public static ReplyCommentRegisterResponse toReplyCommentRegisterResponse(ReplyComment replyComment){
		return new ReplyCommentRegisterResponse(
				replyComment.getId()
		);
	}

	public static ReplyCommentGetResponse toReplyCommentGetResponse(ReplyComment replyComment, boolean isEditable){
		return new ReplyCommentGetResponse(
				replyComment.getId(),
				replyComment.getMember().getNickname(),
				replyComment.getMember().getProfileImage(),
				replyComment.getContent(),
				replyComment.getCreatedAt(),
				replyComment.getUpdatedAt(),
				isEditable
		);
	}

	public static ReplyCommentGetAllResponse toReplyCommentGetAllResponse(
			List<ReplyComment> replyComments, List<Boolean> isEditables, long lastId
	){
		List<ReplyCommentGetResponse> replyCommentGetResponses = new ArrayList<>();
		for (int i = 0; i < replyComments.size(); i++) {
			replyCommentGetResponses.add(toReplyCommentGetResponse(replyComments.get(i), isEditables.get(i)));
		}
		return new ReplyCommentGetAllResponse(
			replyCommentGetResponses,
			lastId
		);
	}
}
