package com.algaworks.fastpay.application.exception;

import lombok.Getter;

@Getter
public class CreditCardAlreadyAssignedCustomerException extends RuntimeException {
	private final String cardId;
	public CreditCardAlreadyAssignedCustomerException(String message, String cardId) {
		super(message);
		this.cardId = cardId;
	}
}
