package com.algaworks.fastpay.application.model;

import com.algaworks.fastpay.domain.model.payment.Payment;
import com.algaworks.fastpay.domain.model.payment.PaymentMethod;
import com.algaworks.fastpay.domain.model.payment.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
public class PaymentModel {

	private String id;
	private BigDecimal totalAmount;

	private PaymentStatus status;
	private PaymentMethod method;

	private String fullName;
	private String document;
	private String phone;

	private String addressLine1;
	private String addressLine2;

	private String zipCode;

	private String tokenizedCreditCardId;
	private String referenceCode;

	private String replyToUrl;

	private OffsetDateTime createdAt;
	private OffsetDateTime paidAt;
	private OffsetDateTime refundAt;
	private OffsetDateTime expiresAt;

	public static PaymentModel.PaymentModelBuilder of(Payment payment) {
		return PaymentModel.builder()
				.id(payment.getId())
				.totalAmount(payment.getTotalAmount())
				.status(payment.getStatus())
				.method(payment.getMethod())
				.fullName(payment.getFullName())
				.document(payment.getDocument())
				.phone(payment.getPhone())
				.addressLine1(payment.getAddressLine1())
				.addressLine2(payment.getAddressLine2())
				.zipCode(payment.getZipCode())
				.tokenizedCreditCardId(payment.getTokenizedCreditCardId())
				.referenceCode(payment.getReferenceCode())
				.replyToUrl(payment.getReplyToUrl())
				.createdAt(payment.getCreatedAt())
				.paidAt(payment.getPaidAt())
				.refundAt(payment.getRefundAt())
				.expiresAt(payment.getExpiresAt());
	}
}
