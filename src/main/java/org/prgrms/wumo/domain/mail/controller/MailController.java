package org.prgrms.wumo.domain.mail.controller;

import org.prgrms.wumo.domain.mail.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/emails")
@RequiredArgsConstructor
@Tag(name = "메일 API")
public class MailController {

	private final MailService mailService;

	@GetMapping
	@Operation(summary = "이메일 인증코드 전송")
	public ResponseEntity<Void> sendMail(
		@RequestParam("address") @Parameter(description = "이메일 인증을 원하는 회원의 이메일 주소") String toAddress) {

		mailService.sendCodeMail(toAddress);
		return ResponseEntity.ok().build();
	}
}
