package org.prgrms.wumo.global.exception.custom;

public class ExpiredRefreshTokenException extends ExpiredTokenException {
	public ExpiredRefreshTokenException() {
	}

	public ExpiredRefreshTokenException(String message) {
		super(message);
	}
}
