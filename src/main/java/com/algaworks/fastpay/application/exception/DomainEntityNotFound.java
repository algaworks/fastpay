package com.algaworks.fastpay.application.exception;

public class DomainEntityNotFound extends RuntimeException {
	public DomainEntityNotFound() {
	}

	public DomainEntityNotFound(String message) {
		super(message);
	}

	public DomainEntityNotFound(String message, Throwable cause) {
		super(message, cause);
	}

	public DomainEntityNotFound(Throwable cause) {
		super(cause);
	}

	public DomainEntityNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
