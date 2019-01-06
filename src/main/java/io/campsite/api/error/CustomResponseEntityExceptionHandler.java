package io.campsite.api.error;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mongodb.MongoWriteException;

import io.campsite.api.model.ApiErrorDto;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ApiValidationException.class)
	public final ResponseEntity<ApiErrorDto> handleValidationException(ApiValidationException ex, WebRequest request) {
		ApiErrorDto error = new ApiErrorDto();
		error.setMessage(ex.getMessage());
		return new ResponseEntity<ApiErrorDto>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ApiNotFoundException.class)
	public final ResponseEntity<ApiErrorDto> handleValidationException(ApiNotFoundException ex, WebRequest request) {
		ApiErrorDto error = new ApiErrorDto();
		error.setMessage(ex.getMessage());
		return new ResponseEntity<ApiErrorDto>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MongoWriteException.class)
	public final ResponseEntity<ApiErrorDto> handleValidationException(MongoWriteException ex, WebRequest request) {
		String errorMessage;
		if (ex.getMessage().contains("duplicate key error collection")) {
			errorMessage = "date not available";
			Matcher matcher = Pattern.compile("(.)*(\\\"){1}([0-9\\-]+)(\\\"){1}(.)*").matcher(ex.getMessage());
			if (matcher.find() && matcher.groupCount() >= 3) {
				errorMessage += ": " + matcher.group(3);
			}
		} else {
			errorMessage = "error saving reservation";
		}
		ApiErrorDto error = new ApiErrorDto();
		error.setMessage(errorMessage);
		return new ResponseEntity<ApiErrorDto>(error, HttpStatus.BAD_REQUEST);
	}

}
