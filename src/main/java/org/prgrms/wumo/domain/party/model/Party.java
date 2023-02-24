package org.prgrms.wumo.domain.party.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

	@Column(name = "description", nullable = true, unique = false, length = 255)
	private String description;

	@Column(name = "image_url", nullable = false, unique = false, length = 255)
	private String coverImage;

	@Column(name = "password", nullable = true, unique = false, length = 4)
	private String password;

	@Builder
	public Party(Long id, String name, LocalDateTime startDate, LocalDateTime endDate, String description,
		String coverImage,
		String password) {
		Assert.isTrue(startDate.isBefore(endDate), "종료일이 시작일보다 빠를 수 없습니다.");

		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.coverImage = coverImage;
		this.password = password;
	}

	// TODO : 입장 시 비밀번호 체크 로직
	public boolean checkPassword(String password) {
		return true;
	}

}
