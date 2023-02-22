package org.prgrms.wumo.domain.route.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.global.audit.BaseTimeEntity;

import lombok.AccessLevel;
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

	private boolean isPublic;

	private long likeCount;

	@OneToMany(mappedBy = "route")
	private List<Location> locations;

	@OneToOne
	private Party party;

	public void changeRoute(Location location) {
		if (location.getRoute() != null) {
			location.getRoute().getLocations().remove(location);
		}
		location.addRoute(this);
		this.locations.add(location);
	}
}
