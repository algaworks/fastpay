package com.algaworks.fastpay.domain.model.payment;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Locale;

@Data
@EqualsAndHashCode
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Payment {

	@Id
	@Builder.Default
	private String id = "pay_" + RandomStringUtils.secure().nextAlphanumeric(32).toLowerCase(Locale.ROOT);

	private BigDecimal totalAmount;

	@CreatedDate
	private OffsetDateTime createdAt;

	@LastModifiedDate
	private OffsetDateTime lastModifiedAt;

	@Version
	private Long version;

	@Enumerated(EnumType.STRING)
	private PaymentStatus status;

	@Enumerated(EnumType.STRING)
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

	private OffsetDateTime paidAt;
	private OffsetDateTime failedAt;
	private OffsetDateTime refundAt;
	private OffsetDateTime expiresAt;

	private boolean expired;

	private boolean notified;

	public void updateStatus(PaymentStatus status) {
		this.status = status;
		if (status == PaymentStatus.PAID) {
			this.paidAt = OffsetDateTime.now();
		} else if (status == PaymentStatus.FAILED) {
			this.failedAt = OffsetDateTime.now();
		} else if (status == PaymentStatus.REFUNDED) {
			this.refundAt = OffsetDateTime.now();
		}
	}

}
