package org.prgrms.wumo.domain.comment.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.party.model.PartyMember;
import static lombok.AccessLevel.PROTECTED;

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
	public LocationComment(Long id, Member member, String content, ContentType contentType, Long locationId,
			PartyMember partyMember, boolean isEdited) {
		super(id, member, content, contentType, isEdited);
		this.locationId = locationId;
		this.partyMember = partyMember;
	}
}
