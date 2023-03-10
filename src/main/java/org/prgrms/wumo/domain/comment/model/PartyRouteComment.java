package org.prgrms.wumo.domain.comment.model;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.prgrms.wumo.domain.comment.dto.request.PartyRouteCommentUpdateRequest;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.party.model.PartyMember;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "party_route_comment")
@DiscriminatorValue("Party_Route")
@NoArgsConstructor(access = PROTECTED)
public class PartyRouteComment extends Comment {

	@Column(name = "route_id", nullable = false, updatable = true, unique = false)
	private Long routeId;

	@Column(name = "location_id", nullable = false, updatable = false, unique = false)
	private Long locationId;

	@OneToOne
	@JoinColumn(name = "party_member_id")
	private PartyMember partyMember;

	@Builder
	public PartyRouteComment(Long id, Member member, String content, String image, Long routeId,
			PartyMember partyMember, Long locationId) {
		super(id, member, content, image);
		this.routeId = routeId;
		this.partyMember = partyMember;
		this.locationId = locationId;
	}

	public void setPartyMember(PartyMember partyMember) {
		this.partyMember = partyMember;
	}

	public void update(PartyRouteCommentUpdateRequest partyRouteCommentUpdateRequest) {
		this.image =
				partyRouteCommentUpdateRequest.image() == null ? this.getImage() : partyRouteCommentUpdateRequest.image();
		this.content =
				partyRouteCommentUpdateRequest.content() == null ? this.getContent() : partyRouteCommentUpdateRequest.content();
	}
}
