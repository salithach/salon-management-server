package com.salonhq.server.exception;

import com.salonhq.server.model.response.EnvelopedResponse;
import com.salonhq.server.model.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class CustomExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<?> handleAllExceptions(Exception ex) {
		LOGGER.error(INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
		EnvelopedResponse<Object> envelopedResponse = new EnvelopedResponse<>();
		envelopedResponse.setErrors(List.of(new ErrorResponse(INTERNAL_SERVER_ERROR.value(), ex.getMessage())));
		return new ResponseEntity<>(envelopedResponse, INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(CredentialException.class)
	public ResponseEntity<?> handleCredentialException(CredentialException ex) {
		EnvelopedResponse<Object> envelopedError = new EnvelopedResponse<>();
		envelopedError.setErrors(List.of(new ErrorResponse(BAD_REQUEST.value(), ex.getMessage())));
		return ResponseEntity.status(BAD_REQUEST).body(envelopedError);
	}

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
		List<ErrorResponse> errorList = ex.getBindingResult().getFieldErrors().stream()
				.map(FieldError::getDefaultMessage)
				.map(msg -> new ErrorResponse(BAD_REQUEST.value(), msg))
				.toList();
		EnvelopedResponse<Object> envelopedResponse = new EnvelopedResponse<>();
		envelopedResponse.setErrors(errorList);
		return new ResponseEntity<>(envelopedResponse, BAD_REQUEST);
	}

	@ExceptionHandler(AuthorizationDeniedException.class)
	public final ResponseEntity<?> handleAuthorizationException(AuthorizationDeniedException ex) {
		EnvelopedResponse<Object> envelopedResponse = new EnvelopedResponse<>();
		envelopedResponse.setErrors(List.of(new ErrorResponse(FORBIDDEN.value(), ex.getMessage())));
		return new ResponseEntity<>(envelopedResponse, FORBIDDEN);
	}

	@ExceptionHandler(AuthenticationException.class)
	public final ResponseEntity<?> handleJwtException(AuthenticationException ex) {
		EnvelopedResponse<Object> envelopedResponse = new EnvelopedResponse<>();
		envelopedResponse.setErrors(List.of(new ErrorResponse(UNAUTHORIZED.value(), ex.getMessage())));
		return new ResponseEntity<>(envelopedResponse, UNAUTHORIZED);
	}
}
