package org.prgrms.wumo.global.exception.custom;

public class DuplicateException extends RuntimeException {
	public DuplicateException() {
	}

	public DuplicateException(String message) {
		super(message);
	}
}
