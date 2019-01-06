package io.campsite.api.error;

public class ApiValidationException extends Exception {

	private static final long serialVersionUID = 2356820980963093876L;

	public ApiValidationException(String message) {
		super(message);
	}

}
