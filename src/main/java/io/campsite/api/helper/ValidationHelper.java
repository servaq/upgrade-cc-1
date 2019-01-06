package io.campsite.api.helper;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import io.campsite.api.error.ApiValidationException;

public abstract class ValidationHelper {

	public static boolean isEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

	public static String required(String value, String fieldName) throws ApiValidationException {
		if (value == null || value.trim().isEmpty()) {
			throw new ApiValidationException(fieldName + " is required");
		}
		return value.trim();
	}

	public static void requireContent(String value, String contentRequired, String fieldName)
			throws ApiValidationException {
		if (value == null || value.trim().isEmpty() || !value.contains(contentRequired)) {
			throw new ApiValidationException(fieldName + " requires '" + contentRequired + "'");
		}
	}

	public static LocalDate requireValidDate(String value, String fieldName) throws ApiValidationException {
		try {
			return LocalDate.parse(value);
		} catch (DateTimeParseException e) {
			throw new ApiValidationException(fieldName + " requires valid format YYYY-MM-DD");
		}
	}

}
