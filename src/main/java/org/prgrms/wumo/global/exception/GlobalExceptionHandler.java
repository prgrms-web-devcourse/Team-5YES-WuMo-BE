package org.prgrms.wumo.global.exception;

import org.prgrms.wumo.global.exception.custom.DuplicateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({DuplicateException.class, ImageUploadFailedException.class, InvalidImageFormatException.class})
	public ResponseEntity<ExceptionResponse> handleException(RuntimeException runtimeException) {
		log.info("exception : " + runtimeException);
		return ResponseEntity.badRequest().body(new ExceptionResponse(runtimeException.getMessage()));
	}

}
