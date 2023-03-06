package org.prgrms.wumo.global.exception.custom;

public class InvalidCodeException extends RuntimeException {
	public InvalidCodeException() {
	}

	public InvalidCodeException(String message) {
		super(message);
	}
}
