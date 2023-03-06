package org.prgrms.wumo.domain.member.api;

import javax.validation.Valid;

import org.prgrms.wumo.domain.member.dto.request.MemberCodeCheckRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberEmailCheckRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberLoginRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberNicknameCheckRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberRegisterRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberReissueRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberUpdateRequest;
import org.prgrms.wumo.domain.member.dto.response.MemberGetResponse;
import org.prgrms.wumo.domain.member.dto.response.MemberLoginResponse;
import org.prgrms.wumo.domain.member.dto.response.MemberRegisterResponse;
import org.prgrms.wumo.domain.member.service.MemberService;
import org.prgrms.wumo.global.util.MailSender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "회원 API")
public class MemberController {

	private final MemberService memberService;
	private final MailSender mailSender;

	@GetMapping("/send-code")
	@Operation(summary = "이메일 인증코드 전송")
	public ResponseEntity<Void> sendCodeMail(
		@RequestParam("address") @Parameter(description = "이메일 인증을 원하는 회원의 이메일 주소") String toAddress) {

		mailSender.sendCodeMail(toAddress);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/check-code")
	@Operation(summary = "이메일 인증코드 검증")
	public ResponseEntity<Void> checkCodeMail(
		@Valid MemberCodeCheckRequest memberCodeCheckRequest) {

		memberService.checkCodeMail(memberCodeCheckRequest.address(), memberCodeCheckRequest.code());
		return ResponseEntity.ok().build();
	}

	@PostMapping("/signup")
	@Operation(summary = "회원가입")
	public ResponseEntity<MemberRegisterResponse> registerMember(
		@RequestBody @Valid MemberRegisterRequest memberRegisterRequest) {

		return new ResponseEntity<>(memberService.registerMember(memberRegisterRequest), HttpStatus.CREATED);
	}

	@GetMapping("/check-email")
	@Operation(summary = "이메일 중복체크")
	public ResponseEntity<Void> checkEmail(
		@Valid MemberEmailCheckRequest memberEmailCheckRequest) {

		memberService.checkEmail(memberEmailCheckRequest.email());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/check-nickname")
	@Operation(summary = "닉네임 중복체크")
	public ResponseEntity<Void> checkNickname(
		@Valid MemberNicknameCheckRequest memberNicknameCheckRequest) {

		memberService.checkNickname(memberNicknameCheckRequest.nickname());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PostMapping("/login")
	@Operation(summary = "로그인")
	public ResponseEntity<MemberLoginResponse> loginMember(
		@RequestBody @Valid MemberLoginRequest memberLoginRequest) {

		return ResponseEntity.ok(memberService.loginMember(memberLoginRequest));
	}

	@DeleteMapping("/logout")
	@Operation(summary = "로그아웃")
	public ResponseEntity<Void> logoutMember() {

		memberService.logoutMember();
		return ResponseEntity.ok().build();
	}

	@PostMapping("/reissue")
	@Operation(summary = "토큰 재발급")
	public ResponseEntity<MemberLoginResponse> reissueMember(
		@RequestBody @Valid MemberReissueRequest memberReissueRequest
	) {

		return ResponseEntity.ok(memberService.reissueMember(memberReissueRequest));
	}

	@GetMapping("/{memberId}")
	@Operation(summary = "내 정보 조회")
	public ResponseEntity<MemberGetResponse> getMember(
		@PathVariable @Parameter(description = "조회할 회원 아이디") long memberId) {

		return ResponseEntity.ok(memberService.getMember(memberId));
	}

	@PatchMapping
	@Operation(summary = "내 정보 수정")
	public ResponseEntity<MemberGetResponse> updateMember(
		@RequestBody @Valid MemberUpdateRequest memberUpdateRequest) {

		return ResponseEntity.ok(memberService.updateMember(memberUpdateRequest));
	}
}
