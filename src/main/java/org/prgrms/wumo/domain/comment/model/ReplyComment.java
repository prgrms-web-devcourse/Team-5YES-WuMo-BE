package org.prgrms.wumo.domain.comment.model;

import static lombok.AccessLevel.PROTECTED;

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

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "reply_comment")
@NoArgsConstructor(access = PROTECTED)
public class ReplyComment extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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
}
