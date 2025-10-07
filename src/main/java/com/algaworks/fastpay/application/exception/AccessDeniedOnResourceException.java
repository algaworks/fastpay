package com.algaworks.fastpay.application.exception;

public class AccessDeniedOnResourceException extends RuntimeException {
	public AccessDeniedOnResourceException() {
	}

	public AccessDeniedOnResourceException(String message) {
		super(message);
	}

	public AccessDeniedOnResourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccessDeniedOnResourceException(Throwable cause) {
		super(cause);
	}

	public AccessDeniedOnResourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
