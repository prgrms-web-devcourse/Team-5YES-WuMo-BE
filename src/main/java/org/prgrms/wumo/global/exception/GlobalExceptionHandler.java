package org.prgrms.wumo.global.exception;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.global.exception.custom.DuplicateException;
import org.prgrms.wumo.global.exception.custom.ExpiredInvitationException;
import org.prgrms.wumo.global.exception.custom.ExpiredTokenException;
import org.prgrms.wumo.global.exception.custom.ImageDeleteFailedException;
import org.prgrms.wumo.global.exception.custom.ImageUploadFailedException;
import org.prgrms.wumo.global.exception.custom.InvalidCodeException;
import org.prgrms.wumo.global.exception.custom.PartyNotEmptyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({
			ExpiredTokenException.class
	})
	public ResponseEntity<ExceptionResponse> handleRefreshTokenException(RuntimeException runtimeException) {
		log.info("exception : " + runtimeException);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ExceptionResponse(runtimeException.getMessage()));
	}

	@ExceptionHandler({
			AccessDeniedException.class
	})
	public ResponseEntity<ExceptionResponse> handleAccessException(RuntimeException runtimeException) {
		log.info("exception : " + runtimeException);
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new ExceptionResponse(runtimeException.getMessage()));
	}

	@ExceptionHandler({
			DuplicateException.class
	})
	public ResponseEntity<ExceptionResponse> handleDuplicateException(RuntimeException runtimeException) {
		log.info("exception : " + runtimeException);
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new ExceptionResponse(runtimeException.getMessage()));
	}

	@ExceptionHandler({
			ImageUploadFailedException.class, IllegalArgumentException.class, ImageDeleteFailedException.class,
			EntityNotFoundException.class, PartyNotEmptyException.class, ExpiredInvitationException.class,
			MailSendException.class, InvalidCodeException.class, BadCredentialsException.class
	})
	public ResponseEntity<ExceptionResponse> handleException(RuntimeException runtimeException) {
		log.info("exception : " + runtimeException);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ExceptionResponse(runtimeException.getMessage()));
	}

	@ExceptionHandler({
			BindException.class
	})
	public ResponseEntity<ExceptionResponse> handleBindException(BindException bindException) {
		log.info("exception : " + bindException);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ExceptionResponse(bindException.getAllErrors().get(0).getDefaultMessage()));
	}

	@ExceptionHandler({
			Exception.class
	})
	public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
		log.error("exception : " + exception);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ExceptionResponse(exception.getMessage()));
	}

}
