package com.algaworks.fastpay.domain.model.payment;

public enum PaymentStatus {
	PENDING,
	PROCESSING,
	FAILED,
	PAID,
	CANCELLED,
	REFUNDED;
}
