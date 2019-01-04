package io.campsite.api.error;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 2356820980963093876L;

	public ValidationException(String message) {
		super(message);
	}

}
