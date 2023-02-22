package org.prgrms.wumo.domain.party.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.prgrms.wumo.domain.party.model.Party;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invitation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "party_id", nullable = false)
	private Party party;

	@Column(name = "expired_date", nullable = false)
	private LocalDate expiredDate;

	@Column(name = "code", nullable = false, length = 255)
	private String code; // Unique

	@Builder
	public Invitation(Long id, Party party, LocalDate expiredDate, String code) {
		this.id = id;
		this.party = party;
		this.expiredDate = expiredDate;
		this.code = code;
	}

}
