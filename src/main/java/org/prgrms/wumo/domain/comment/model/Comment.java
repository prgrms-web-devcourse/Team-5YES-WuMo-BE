package org.prgrms.wumo.domain.comment.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.global.audit.BaseTimeEntity;
import static lombok.AccessLevel.PROTECTED;

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

	@Column(name = "content", nullable = false, unique = false, updatable = true, length = 255)
	protected String content;

	@Enumerated(EnumType.STRING)
	@Column(name = "content_type", nullable = false, updatable = true, unique = false)
	protected ContentType contentType;

	@Column(name = "is_edited", nullable = false, updatable = true, unique = false)
	protected boolean isEdited;

	@Builder
	public Comment(Long id, Member member, String content, ContentType contentType, boolean isEdited) {
		this.id = id;
		this.member = member;
		this.content = content;
		this.contentType = contentType;
		this.isEdited = isEdited;
	}
}
