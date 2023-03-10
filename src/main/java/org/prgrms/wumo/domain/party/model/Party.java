package org.prgrms.wumo.domain.party.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.prgrms.wumo.global.audit.BaseTimeEntity;
import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "party")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Party extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false, unique = false, length = 40)
	private String name;

	@Column(name = "start_date", nullable = false, unique = false)
	private LocalDateTime startDate;

	@Column(name = "end_date", nullable = false, unique = false)
	private LocalDateTime endDate;

	@Column(name = "description", nullable = true, unique = false, length = 100)
	private String description;

	@Column(name = "image_url", nullable = false, unique = false, length = 255)
	private String coverImage;

	@Transient
	private long totalMembers;

	@Transient
	private List<PartyMember> partyMembers;

	@Builder
	public Party(Long id, String name, LocalDateTime startDate, LocalDateTime endDate, String description, String coverImage) {
		Assert.isTrue(startDate.isBefore(endDate) || startDate.isEqual(endDate), "종료일이 시작일보다 빠를 수 없습니다.");

		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.coverImage = coverImage;
	}

	public void update(String name, LocalDateTime startDate, LocalDateTime endDate, String description, String coverImage) {
		this.name = Objects.requireNonNullElse(name, this.name);
		if (startDate != null) {
			Assert.isTrue(startDate.isBefore(endDate) || startDate.isEqual(endDate), "종료일이 시작일보다 빠를 수 없습니다.");
			this.startDate = startDate;
		}
		if (endDate != null) {
			Assert.isTrue(endDate.isAfter(this.startDate) || endDate.isEqual(this.startDate), "시작일이 종료일보다 느릴 수 없습니다.");
			this.endDate = endDate;
		}
		this.description = Objects.requireNonNullElse(description, this.description);
		this.coverImage = Objects.requireNonNullElse(coverImage, this.coverImage);
	}

	public void setTotalMembers(long totalMembers) {
		this.totalMembers = totalMembers;
	}

	public void setPartyMembers(List<PartyMember> partyMembers) {
		this.partyMembers = partyMembers;
	}

}
