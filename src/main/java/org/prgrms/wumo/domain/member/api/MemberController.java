package org.prgrms.wumo.domain.member.api;

import static org.prgrms.wumo.domain.member.mapper.MemberMapper.toMemberLoginResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.prgrms.wumo.domain.member.dto.request.MemberCodeCheckRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberEmailCheckRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberLoginRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberNicknameCheckRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberPasswordUpdateRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberRegisterRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberReissueRequest;
import org.prgrms.wumo.domain.member.dto.request.MemberUpdateRequest;
import org.prgrms.wumo.domain.member.dto.response.MemberGetResponse;
import org.prgrms.wumo.domain.member.dto.response.MemberLoginResponse;
import org.prgrms.wumo.domain.member.dto.response.MemberRegisterResponse;
import org.prgrms.wumo.domain.member.dto.response.MemberTokenResponse;
import org.prgrms.wumo.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

	@GetMapping("/send-code")
	@Operation(summary = "이메일 인증코드 전송")
	public ResponseEntity<Void> sendCode(
			@RequestParam("address") @Parameter(description = "인증코드를 받을 회원의 이메일 주소") String toAddress) {

		memberService.sendCode(toAddress);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/check-code")
	@Operation(summary = "이메일 인증코드 검증")
	public ResponseEntity<Void> checkCode(
			@Valid MemberCodeCheckRequest memberCodeCheckRequest) {

		memberService.checkCode(memberCodeCheckRequest.address(), memberCodeCheckRequest.code());
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
		return ResponseEntity.ok().build();
	}

	@GetMapping("/check-nickname")
	@Operation(summary = "닉네임 중복체크")
	public ResponseEntity<Void> checkNickname(
			@Valid MemberNicknameCheckRequest memberNicknameCheckRequest) {

		memberService.checkNickname(memberNicknameCheckRequest.nickname());
		return ResponseEntity.ok().build();
	}

	@PostMapping("/login")
	@Operation(summary = "로그인")
	public ResponseEntity<MemberLoginResponse> loginMember(
			@RequestBody @Valid MemberLoginRequest memberLoginRequest,
			HttpServletResponse httpServletResponse) {

		MemberTokenResponse memberTokenResponse = memberService.loginMember(memberLoginRequest);
		httpServletResponse.addCookie(generateTokenCookie(memberTokenResponse.refreshToken()));
		return ResponseEntity.ok(toMemberLoginResponse(memberTokenResponse));
	}

	@DeleteMapping("/logout")
	@Operation(summary = "로그아웃")
	public ResponseEntity<Void> logoutMember(
			HttpServletResponse httpServletResponse) {

		memberService.logoutMember();
		httpServletResponse.addCookie(generateExpiredTokenCookie());
		return ResponseEntity.ok().build();
	}

	@PostMapping("/reissue")
	@Operation(summary = "토큰 재발급")
	public ResponseEntity<MemberLoginResponse> reissueMember(
			@CookieValue String refreshToken,
			@RequestBody @Valid MemberReissueRequest memberReissueRequest,
			HttpServletResponse httpServletResponse) {

		MemberTokenResponse memberTokenResponse = memberService.reissueMember(memberReissueRequest, refreshToken);
		httpServletResponse.addCookie(generateTokenCookie(refreshToken));
		return ResponseEntity.ok(toMemberLoginResponse(memberTokenResponse));
	}

	@GetMapping("/me")
	@Operation(summary = "내 정보 조회")
	public ResponseEntity<MemberGetResponse> getMember() {

		return ResponseEntity.ok(memberService.getMember());
	}

	@PatchMapping("/me")
	@Operation(summary = "내 정보 수정")
	public ResponseEntity<MemberGetResponse> updateMember(
			@RequestBody @Valid MemberUpdateRequest memberUpdateRequest) {

		return ResponseEntity.ok(memberService.updateMember(memberUpdateRequest));
	}

	@PatchMapping
	@Operation(summary = "내 비밀번호 수정")
	public ResponseEntity<Void> updateMemberPassword(
			@RequestBody @Valid MemberPasswordUpdateRequest memberPasswordUpdateRequest) {

		memberService.updateMemberPassword(memberPasswordUpdateRequest);
		return ResponseEntity.ok().build();
	}

	private Cookie generateTokenCookie(String refreshToken) {
		Cookie cookie = new Cookie("refreshToken", refreshToken);
		cookie.setMaxAge(60 * 60 * 24 * 7);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		return cookie;
	}

	private Cookie generateExpiredTokenCookie() {
		Cookie cookie = new Cookie("refreshToken", "");
		cookie.setMaxAge(0);
		return cookie;
	}

}
