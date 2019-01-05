package io.campsite.api.helper;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import io.campsite.api.error.ValidationException;

public abstract class ValidationHelper {

	public static String required(String value, String fieldName) throws ValidationException {
		if (value == null || value.trim().isEmpty()) {
			throw new ValidationException(fieldName + " is required");
		}
		return value.trim();
	}

	public static void requireContent(String value, String contentRequired, String fieldName)
			throws ValidationException {
		if (value == null || value.trim().isEmpty() || !value.contains(contentRequired)) {
			throw new ValidationException(fieldName + " require '" + contentRequired + "'");
		}
	}

	public static LocalDate requireValidDate(String value, String fieldName) throws ValidationException {
		try {
			return LocalDate.parse(value);
		} catch (DateTimeParseException e) {
			throw new ValidationException(fieldName + " require valid format YYYY-MM-DD");
		}
	}

}
