package io.campsite.api.error;

public class ApiErrorDetails {

	private String message;
	private String details;

	public ApiErrorDetails(String message, String details) {
		super();
		this.message = message;
		this.details = details;
	}

}
