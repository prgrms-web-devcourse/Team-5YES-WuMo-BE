package org.prgrms.wumo.domain.comment.api;

import javax.validation.Valid;

import org.prgrms.wumo.domain.comment.dto.request.ReplyCommentRegisterRequest;
import org.prgrms.wumo.domain.comment.dto.response.ReplyCommentRegisterResponse;
import org.prgrms.wumo.domain.comment.service.ReplyCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reply-comments")
@Tag(name = "대댓글 api")
public class ReplyCommentController {
		private final ReplyCommentService replyCommentService;

	@PostMapping("/location-comments")
	@Operation(summary = "후보지 댓글에 대댓글 작성")
	public ResponseEntity<ReplyCommentRegisterResponse> registerReplyComment(
			@RequestBody @Valid ReplyCommentRegisterRequest replyCommentRegisterRequest
	) {
		return new ResponseEntity<>(replyCommentService.registerReplyComment(replyCommentRegisterRequest),
				HttpStatus.CREATED);
	}
}
