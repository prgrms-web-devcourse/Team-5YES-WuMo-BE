package org.prgrms.wumo.domain.like.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "route_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "route_id", nullable = false, updatable = false, unique = false)
	private Long routeId;

	@Column(name = "member_id", nullable = false, updatable = false, unique = false)
	private Long memberId;

	@Builder
	public RouteLike(Long id, Long routeId, Long memberId) {
		this.id = id;
		this.routeId = routeId;
		this.memberId = memberId;
	}

}
