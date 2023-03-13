package org.prgrms.wumo.domain.comment.model;

import static lombok.AccessLevel.PROTECTED;
import static org.prgrms.wumo.global.exception.ExceptionMessage.WRONG_ACCESS;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.global.audit.BaseTimeEntity;
import org.springframework.security.access.AccessDeniedException;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comment")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorColumn
public class Comment extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@OneToOne
	@JoinColumn(name = "member_id")
	protected Member member;

	@Column(name = "content", nullable = true, unique = false, updatable = true, length = 255)
	protected String content;

	@Column(name = "image_url", nullable = true, unique = true, updatable = true, length = 255)
	protected String image;

	public Comment(Long id, Member member, String content, String image) {
		this.id = id;
		this.member = member;
		this.content = content;
		this.image = image;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public void checkAuthorization(Long memberId) {
		if (!Objects.equals(this.member.getId(), memberId)) {
			throw new AccessDeniedException(WRONG_ACCESS.name());
		}
	}
}
