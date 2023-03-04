package org.prgrms.wumo.domain.member.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.prgrms.wumo.global.audit.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Email email;

	@Column(name = "nickname", nullable = false, updatable = true, unique = true, length = 20)
	private String nickname;

	@Embedded
	private Password password;

	@Column(name = "image_url", nullable = true, updatable = true, unique = false)
	private String profileImage;

	@Builder
	public Member(Long id, String email, String nickname, String password, String profileImage) {
		this.id = id;
		this.email = new Email(email);
		this.nickname = nickname;
		this.password = new Password(password);
		this.profileImage = profileImage;
	}

	public boolean isNotValidPassword(String inputPassword) {
		return !password.isValidPassword(inputPassword);
	}

	public void update(String nickname, String password, String profileImage) {
		this.nickname = nickname;
		this.password = new Password(password);
		this.profileImage = profileImage;
	}
}
