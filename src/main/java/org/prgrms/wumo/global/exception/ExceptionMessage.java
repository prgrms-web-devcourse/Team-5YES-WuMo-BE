package org.prgrms.wumo.global.exception;

public enum ExceptionMessage {
	MEMBER("회원"),
	PARTY("모임"),
	PARTY_MEMBER("모임 멤버"),
	LOCATION("후보지"),
	ROUTE("루트"),
	COMMENT("댓글"),
	ENTITY_NOT_FOUND("존재하지 않는 %s 입니다."),
	WRONG_ACCESS("잘못된 접근입니다.");

	private final String message;

	ExceptionMessage(String message) {
		this.message = message;
	}
}
