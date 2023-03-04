package org.prgrms.wumo.global.exception.custom;

public class InvalidTokenException extends TokenException {
	public InvalidTokenException() {
	}

	public InvalidTokenException(String message) {
		super(message);
	}
}
