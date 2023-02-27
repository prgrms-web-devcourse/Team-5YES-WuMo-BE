package org.prgrms.wumo.global.exception;

import org.prgrms.wumo.global.exception.custom.DuplicateException;
import org.prgrms.wumo.global.exception.custom.ImageDeleteFailedException;
import org.prgrms.wumo.global.exception.custom.ImageUploadFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({
		DuplicateException.class
	})
	public ResponseEntity<ExceptionResponse> handleDuplicateException(RuntimeException runtimeException) {
		log.info("exception : " + runtimeException);
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(new ExceptionResponse(runtimeException.getMessage()));
	}

	@ExceptionHandler({
		ImageUploadFailedException.class, IllegalArgumentException.class, ImageDeleteFailedException.class
	})
	public ResponseEntity<ExceptionResponse> handleException(RuntimeException runtimeException) {
		log.info("exception : " + runtimeException);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ExceptionResponse(runtimeException.getMessage()));
	}

}
