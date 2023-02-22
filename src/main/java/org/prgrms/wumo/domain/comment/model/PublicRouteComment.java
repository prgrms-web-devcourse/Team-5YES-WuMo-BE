package org.prgrms.wumo.domain.comment.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.wumo.domain.member.model.Member;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "public_route_comment")
@DiscriminatorValue("Public_Route")
@NoArgsConstructor(access = PROTECTED)
public class PublicRouteComment extends Comment {

	@Column(name = "route_id", nullable = false, updatable = true, unique = false)
	private Long routeId;

	@Builder
	public PublicRouteComment(Long id, Member member, String content,
			ContentType contentType, Long routeId, boolean isEdited) {
		super(id, member, content, contentType, isEdited);
		this.routeId = routeId;
	}
}
