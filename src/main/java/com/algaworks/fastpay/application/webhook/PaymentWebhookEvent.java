package com.algaworks.fastpay.application.webhook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentWebhookEvent {
	private String paymentId;
	private String referenceCode;
	private String status;
	private String method;
	private OffsetDateTime notifiedAt;
}
