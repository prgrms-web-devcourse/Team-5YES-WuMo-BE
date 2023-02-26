package org.prgrms.wumo.domain.member.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Email 형식이 ")
public class EmailTest {

	@ParameterizedTest
	@DisplayName("올바르지 않으면 예외가 발생한다")
	@ValueSource(strings = {"@@gmail.com", "@gmail..com", "", " ", "123@gmail.", "123.com", "tae"})
	void check_email_pattern(String email) {
		assertThatThrownBy(() -> new Email(email))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("이메일 형식이 올바르지 않습니다.");
	}

}
