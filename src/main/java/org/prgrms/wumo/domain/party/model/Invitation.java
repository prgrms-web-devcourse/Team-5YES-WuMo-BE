package org.prgrms.wumo.domain.party.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.prgrms.wumo.global.audit.BaseTimeEntity;
import org.prgrms.wumo.global.base62.Base62Util;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "invitation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invitation extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "party_id", nullable = false, unique = false)
	private Party party;

	@Column(name = "expired_date", nullable = false, unique = false)
	private LocalDateTime expiredDate;

	@Builder
	public Invitation(Long id, Party party, LocalDateTime expiredDate) {
		this.id = id;
		this.party = party;
		this.expiredDate = expiredDate;
	}

	public String getCode() {
		return Base62Util.encode(this.id);
	}

}
