package io.campsite.api.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.campsite.api.model.ApiErrorDto;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ValidationException.class)
	public final ResponseEntity<ApiErrorDto> handleValidationException(ValidationException ex, WebRequest request) {
		ApiErrorDto error = new ApiErrorDto();
		error.setMessage(ex.getMessage());
		return new ResponseEntity<ApiErrorDto>(error, HttpStatus.BAD_REQUEST);
	}

}
