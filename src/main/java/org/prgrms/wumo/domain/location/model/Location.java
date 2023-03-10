package org.prgrms.wumo.domain.location.model;

import java.time.LocalDateTime;
import java.util.Objects;

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

import org.prgrms.wumo.domain.location.dto.request.LocationUpdateRequest;
import org.prgrms.wumo.domain.route.model.Route;
import org.prgrms.wumo.global.audit.BaseTimeEntity;
import org.springframework.security.access.AccessDeniedException;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "location")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "member_id", nullable = false, updatable = false, unique = false)
	Long memberId;

	@Column(name = "name", nullable = false, updatable = true, unique = false)
	private String name;

	@Column(name = "search_address", nullable = false, updatable = true, unique = false, length = 20)
	private String searchAddress;

	@Column(name = "address", nullable = false, updatable = true, unique = false)
	private String address;

	@Column(name = "latitude", nullable = false, updatable = true, unique = false)
	private Double latitude;

	@Column(name = "longitude", nullable = false, updatable = true, unique = false)
	private Double longitude;

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
	@JoinColumn(name = "route_id", updatable = true, unique = false)
	private Route route;

	@Column(name = "party_id", updatable = false, unique = false)
	private Long partyId;

	@Builder
	public Location(Long id, Long memberId, String name, String address, String searchAddress, Double latitude,
			Double longitude,
			String image,
			String description,
			LocalDateTime visitDate, int expectedCost, int spending, Category category, Route route, Long partyId) {
		this.id = id;
		this.memberId = memberId;
		this.name = name;
		this.searchAddress = searchAddress;
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

	public void addRoute(Route route) {
		this.route = route;
	}

	public void deleteRoute() {
		this.route = null;
	}

	public void update(LocationUpdateRequest request) {
		this.visitDate = request.visitDate();
		this.category = request.category();
		this.image = request.image();
		this.expectedCost = request.expectedCost();
		this.description = request.description() == null ? this.getDescription() : request.description();
	}

	public void updateSpending(int spending) {
		this.spending = spending;
	}

	public void checkAuthorization(Long memberId) {
		if (!Objects.equals(this.memberId, memberId)) {
			throw new AccessDeniedException("후보지는 작성자 및 모임장만 수정 및 삭제할 수 있습니다.");
		}
	}
}
