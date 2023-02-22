package org.prgrms.wumo.domain.party.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.prgrms.wumo.domain.member.model.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne
	@JoinColumn(name = "party_id", nullable = false)
	private Party party;

	@Column(name = "role", nullable = true, length = 10)
	private String role;

	@Column(name = "is_leader", nullable = false)
	private boolean isLeader;

	@Builder
	public PartyMember(Long id, Member member, Party party, String role, boolean isLeader) {
		this.id = id;
		this.member = member;
		this.party = party;
		this.role = role;
		this.isLeader = isLeader;
	}
}
