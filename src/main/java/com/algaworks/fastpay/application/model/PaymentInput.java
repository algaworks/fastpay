package com.algaworks.fastpay.application.model;

import com.algaworks.fastpay.domain.model.payment.Payment;
import com.algaworks.fastpay.domain.model.payment.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentInput {
	@NotNull
	private BigDecimal totalAmount;

	@NotBlank
	private PaymentMethod method;

	@NotBlank
	private String fullName;

	@NotBlank
	private String document;

	@NotBlank
	private String phone;

	@NotBlank
	private String addressLine1;

	private String addressLine2;

	private String creditCardId;

	@NotBlank
	private String referenceCode;

	private String replyToUrl;

	@NotBlank
	private String zipCode;

	public Payment.PaymentBuilder toPayment() {
		return Payment.builder()
				.totalAmount(this.getTotalAmount())
				.method(this.getMethod())
				.fullName(this.getFullName())
				.document(this.getDocument())
				.phone(this.getPhone())
				.addressLine1(this.getAddressLine1())
				.addressLine2(this.getAddressLine2())
				.zipCode(this.getZipCode())
				.referenceCode(this.getReferenceCode())
				.replyToUrl(this.getReplyToUrl())
				.expired(false);
	}
}
