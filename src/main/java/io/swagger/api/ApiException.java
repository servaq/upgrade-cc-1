package io.swagger.api;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-01-04T21:32:25.955Z")

public class ApiException extends Exception {

	private static final long serialVersionUID = -2174637728997742359L;

	private int code;

	public ApiException(int code, String msg) {
		super(msg);
		this.code = code;
	}

}
