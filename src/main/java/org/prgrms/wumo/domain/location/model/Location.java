package org.prgrms.wumo.domain.location.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.wumo.domain.route.model.Route;
import org.prgrms.wumo.global.audit.BaseTimeEntity;

@Getter
@Entity
@Table(name = "location")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false, updatable = true, unique = false)
	private String name;

	@Column(name = "address", nullable = false, updatable = true, unique = false)
	private String address;

	@Column(name = "latitude", nullable = false, updatable = true, unique = false)
	private Float latitude;

	@Column(name = "longitude", nullable = false, updatable = true, unique = false)
	private Float longitude;

	@Column(name = "image_url", nullable = false, updatable = true, unique = false)
	private String image;

	@Column(name = "description", nullable = true, updatable = true, unique = false)
	private String description;

	@Column(name = "visit_date", nullable = false, updatable = true, unique = false)
	private LocalDateTime visitDate;

	@Column(name = "expected_cost", nullable = false, updatable = true, unique = false)
	private int expectedCost;

	@Column(name = "spending", nullable = false, updatable = true, unique = false)
	private int spending;

	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false, updatable = true, unique = false)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "route_id", updatable = false, unique = false)
	private Route route;

	@Column(name = "party_id", updatable = false, unique = false)
	private Long partyId;

	@Builder
	public Location(Long id, String name, String address, Float latitude, Float longitude, String image,
			String description,
			LocalDateTime visitDate, int expectedCost, int spending, Category category, Route route, Long partyId) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.image = image;
		this.description = description;
		this.visitDate = visitDate;
		this.expectedCost = expectedCost;
		this.spending = spending;
		this.category = category;
		this.route = route;
		this.partyId = partyId;
	}

	// TODO 추후 Party 이용, Route 지정할 계획
	public void addRoute(Route route) {
		this.route = route;
	}

}
