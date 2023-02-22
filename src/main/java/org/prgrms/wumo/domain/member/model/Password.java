package org.prgrms.wumo.domain.member.model;

import static lombok.AccessLevel.*;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Password {

	private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,16}$";

	public Password(String password) {
		validatePattern(password);
		this.password = passwordEncoder.encode(password);
	}

	@Column(name = "password", nullable = false, updatable = true, length = 60)
	private String password;

	@Transient
	private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public boolean isValidPassword(String inputPassword) {
		return passwordEncoder.matches(inputPassword, password);
	}

	private void validatePattern(String password) {
		if (!Pattern.matches(PASSWORD_PATTERN, password)) {
			throw new IllegalArgumentException("비밀번호 형식이 올바르지 않습니다.");
		}
	}
}
