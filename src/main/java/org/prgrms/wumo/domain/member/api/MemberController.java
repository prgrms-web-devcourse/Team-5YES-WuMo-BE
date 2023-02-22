package org.prgrms.wumo.domain.member.api;

import javax.validation.Valid;

import org.prgrms.wumo.domain.member.dto.request.MemberEmailCheckRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberLoginRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberNicknameCheckRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberRegisterRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberUpdateRequest;
import org.prgrms.wumo.domain.member.dto.response.MemberGetResponse;
import org.prgrms.wumo.domain.member.dto.response.MemberLoginResponse;
import org.prgrms.wumo.domain.member.dto.response.MemberRegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/members")
@Tag(name = "회원 API")
public class MemberController {

	//////비밀번호 찾기(재설정)

	@PostMapping("/signup")
	@Operation(summary = "회원가입")
	public ResponseEntity<MemberRegisterResponse> registerMember(
		@RequestBody @Valid MemberRegisterRequest memberRegisterRequest) {

		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}

	@PostMapping("/check_email")
	@Operation(summary = "이메일 중복체크")
	public ResponseEntity<Void> checkEmail(
		@RequestBody @Valid MemberEmailCheckRequest memberEmailCheckRequest) {

		return ResponseEntity.ok().build();
	}

	@PostMapping("/check_nickname")
	@Operation(summary = "닉네임 중복체크")
	public ResponseEntity<Void> checkNickname(
		@RequestBody @Valid MemberNicknameCheckRequest memberNicknameCheckRequest) {

		return ResponseEntity.ok().build();
	}

	@PostMapping("/login")
	@Operation(summary = "로그인")
	public ResponseEntity<MemberLoginResponse> loginMember(
		@RequestBody @Valid MemberLoginRequest memberLoginRequest) {

		return ResponseEntity.ok(null);
	}

	@PostMapping("/logout")
	@Operation(summary = "로그아웃")
	public ResponseEntity<Void> logoutMember() {

		return ResponseEntity.ok().build();
	}

	@GetMapping("/{memberId}")
	@Operation(summary = "내 정보 조회")
	public ResponseEntity<MemberGetResponse> getMember(
		@PathVariable @Parameter(description = "조회할 회원 아이디") Long memberId) {

		return ResponseEntity.ok(null);
	}

	@PatchMapping
	@Operation(summary = "내 정보 수정")
	public ResponseEntity<Void> updateMember(
		@RequestBody @Valid MemberUpdateRequest memberUpdateRequest) {

		return ResponseEntity.ok().build();
	}
}
