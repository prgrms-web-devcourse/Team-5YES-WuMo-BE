package org.prgrms.wumo.domain.member.model;

import static lombok.AccessLevel.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Email {

	@Column(name = "email", nullable = false, updatable = false, unique = true)
	private String email;

	public Email(String email) {
		this.email = email;
	}
}
