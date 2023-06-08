package com.picoto.test.exceptions;

public class TestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TestException() {
		super();
	}

	public TestException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public TestException(String message, Throwable cause) {
		super(message, cause);
	}

	public TestException(String message) {
		super(message);
	}

	public TestException(Throwable cause) {
		super(cause);
	}

	
}
