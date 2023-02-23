package org.prgrms.wumo.domain.member.model;

import static lombok.AccessLevel.PROTECTED;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Email {

	private static final String EMAIL_PATTERN = "b[w.-]+@[w.-]+.w{1,10}b";

	@Column(name = "email", nullable = false, updatable = false, unique = true)
	private String email;

	public Email(String email) {
		validEmailPattern(email);
		this.email = email;
	}

	private void validEmailPattern(String email) {
		if (!Pattern.matches(EMAIL_PATTERN, email)) {
			throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
		}
	}
}
