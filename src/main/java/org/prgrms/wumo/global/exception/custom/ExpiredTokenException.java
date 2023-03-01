package org.prgrms.wumo.global.exception.custom;

public class ExpiredTokenException extends TokenException {
	public ExpiredTokenException() {
	}

	public ExpiredTokenException(String message) {
		super(message);
	}
}
