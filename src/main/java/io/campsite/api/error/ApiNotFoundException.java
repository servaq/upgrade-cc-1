package io.campsite.api.error;

public class ApiNotFoundException extends Exception {

	private static final long serialVersionUID = 644099236529180446L;

	public ApiNotFoundException(String message) {
		super(message);
	}

}
