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
	private String latitude;

	@Column(name = "longitude", nullable = false, updatable = true, unique = false)
	private String longitude;

	@Column(name = "image_url", nullable = false, updatable = true, unique = false)
	private String image;

	@Column(name = "description", nullable = true, updatable = true, unique = false)
	private String description;

	@Column(name = "visit_date", nullable = false, updatable = true, unique = false)
	private LocalDateTime visitDate;

	@Column(name = "expectedCost", nullable = false, updatable = true, unique = false)
	private int expectedCost;

	@Column(name = "spending", nullable = false, updatable = true, unique = false)
	private int spending;

	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false, updatable = true, unique = false)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "route_id", updatable = false, unique = false)
	private Route route;

	public void addRoute(Route route) {
		this.route = route;
	}
}
