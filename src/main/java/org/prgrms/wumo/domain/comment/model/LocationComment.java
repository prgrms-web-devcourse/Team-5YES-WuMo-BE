package org.prgrms.wumo.domain.comment.model;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.prgrms.wumo.domain.comment.dto.request.LocationCommentUpdateRequest;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.party.model.PartyMember;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "location_comment")
@DiscriminatorValue("Location")
@NoArgsConstructor(access = PROTECTED)
public class LocationComment extends Comment {

	@Column(name = "location_id", nullable = false, updatable = true, unique = false)
	private Long locationId;

	@OneToOne
	@JoinColumn(name = "party_member_id")
	private PartyMember partyMember;

	@Builder
	public LocationComment(Long id, Member member, String content, String image, Long locationId,
			PartyMember partyMember) {
		super(id, member, content, image);
		this.locationId = locationId;
		this.partyMember = partyMember;
	}

	public void setPartyMember(PartyMember partyMember) {
		this.partyMember = partyMember;
	}

	public void update(LocationCommentUpdateRequest request) {
		this.image = request.image() == null ? this.getImage() : request.image();
		this.content = request.content() == null ? this.getContent() : request.content();
	}
}
