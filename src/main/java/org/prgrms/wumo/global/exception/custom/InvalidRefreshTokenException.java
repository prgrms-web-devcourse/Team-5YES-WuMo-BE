package org.prgrms.wumo.global.exception.custom;

public class InvalidRefreshTokenException extends InvalidTokenException {
	public InvalidRefreshTokenException() {
	}

	public InvalidRefreshTokenException(String message) {
		super(message);
	}
}
