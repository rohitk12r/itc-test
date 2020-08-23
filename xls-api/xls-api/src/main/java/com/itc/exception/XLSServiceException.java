package com.itc.exception;

public class XLSServiceException extends GenericException {

	/**
	 * The serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	public XLSServiceException() {
		super();
	}

	public XLSServiceException(String error, String message, Throwable cause) {
		super(error, message, cause);
	}

	public XLSServiceException(String message) {
		super(message);
	}
}
