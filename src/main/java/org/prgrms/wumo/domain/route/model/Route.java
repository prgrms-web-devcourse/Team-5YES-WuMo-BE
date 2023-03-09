package org.prgrms.wumo.domain.route.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.global.audit.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "route")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Route extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = true, updatable = true)
	private String name;

	@Column(name = "is_public", nullable = false, updatable = true)
	private boolean isPublic;

	@OneToMany(mappedBy = "route")
	@OrderBy(value = "visitDate DESC")
	private List<Location> locations;

	@OneToOne
	private Party party;

	@Column(name = "like_count", nullable = true, updatable = true)
	private long likeCount;

	@Transient
	private boolean isLiking;

	@Builder
	public Route(Long id, String name, List<Location> locations, Party party) {
		this.id = id;
		this.name = name;
		this.isPublic = false;
		this.locations = locations;
		this.party = party;
	}

	public void updateLocation(Location location) {
		if (location.getRoute() != null) {
			location.getRoute().getLocations().remove(location);
		}
		location.addRoute(this);
		this.locations.add(location);
	}

	public void updatePublicStatus(String name, boolean isPublic) {
		this.name = name;
		this.isPublic = isPublic;
	}

	public void addIsLiking(boolean isLiking) {
		this.isLiking = isLiking;
	}
}
