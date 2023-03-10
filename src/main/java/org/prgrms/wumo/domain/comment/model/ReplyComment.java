package org.prgrms.wumo.domain.comment.model;

import static lombok.AccessLevel.PROTECTED;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.global.audit.BaseTimeEntity;
import org.springframework.security.access.AccessDeniedException;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reply_comment")
@NoArgsConstructor(access = PROTECTED)
public class ReplyComment extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(name = "comment_id", nullable = false, unique = false, updatable = false)
	private Long commentId;

	@Column(name = "content", nullable = false, unique = false, updatable = true, length = 255)
	private String content;

	@Builder
	public ReplyComment(Long id, Member member, Long commentId, String content) {
		this.id = id;
		this.member = member;
		this.commentId = commentId;
		this.content = content;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public void checkAuthorization(Long memberId) {
		if (!Objects.equals(this.member.getId(), memberId)) {
			throw new AccessDeniedException("대댓글은 작성자만 수정 및 삭제가 가능합니다.");
		}
	}
}
