package com.algaworks.fastpay.application.scheduler;

import com.algaworks.fastpay.application.service.CreditCardSimulationService;
import com.algaworks.fastpay.application.webhook.PaymentWebhookEvent;
import com.algaworks.fastpay.application.webhook.WebhookClient;
import com.algaworks.fastpay.domain.model.payment.Payment;
import com.algaworks.fastpay.domain.model.payment.PaymentMethod;
import com.algaworks.fastpay.domain.model.payment.PaymentStatus;
import com.algaworks.fastpay.domain.model.payment.PaymentRepository;
import com.algaworks.fastpay.domain.model.creditcard.CreditCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FastPayPaymentWebhookSimulationScheduler {

	private final PaymentRepository paymentRepository;
	private final CreditCardRepository creditCardRepository;
	private final WebhookClient webhookClient;
	private final CreditCardSimulationService creditCardSimulationService;

	@Scheduled(fixedRate = 30000L)
	@Transactional
	public void simulate() {
		log.debug("Sending webhooks");
		List<Payment> payments = paymentRepository.findAllByNotifiedIsFalse(Limit.of(10));

		for (Payment payment : payments) {
			processPayment(payment);
		}

	}

	private void processPayment(Payment payment) {
		if (payment.getMethod().equals(PaymentMethod.CREDIT)) {
			simulateCreditCard(payment);
		} else if (payment.getMethod().equals(PaymentMethod.GATEWAY_BALANCE)) {
			simulateGateway(payment);
		}

		try {
			sendWebhook(payment);
		} catch (Exception e) {
			log.error(String.format("Payment %s failed to notify, error: ", payment.getId()), e);
		}

		payment.setNotified(true);
		paymentRepository.save(payment);
	}

	private void sendWebhook(Payment payment) {
		if (payment.getReplyToUrl() == null || payment.getReplyToUrl().isBlank()) {
			log.info("Payment {} has no webhook URL", payment.getId());
			return;
		}

		log.info("Notifying payment {} as {} on {}", payment.getId(), payment.getStatus(), payment.getReplyToUrl());

		PaymentWebhookEvent webhookEvent = new PaymentWebhookEvent(payment.getId(),
				payment.getReferenceCode(),
				payment.getStatus(),
				OffsetDateTime.now());

		webhookClient.send(webhookEvent, payment.getReplyToUrl());
	}

	private void simulateGateway(Payment payment) {
		payment.setStatus(PaymentStatus.PAID);
		payment.setPaidAt(OffsetDateTime.now());
	}

	private void simulateCreditCard(Payment payment) {
		if (payment.getCreditCardId() == null) {
			log.warn("Payment {} via card but card ID is null", payment.getId());
		}

		var creditCard = creditCardRepository.findById(payment.getCreditCardId());
		if (creditCard.isEmpty()) {
			log.warn("Payment failed invalid card {}.", payment.getCreditCardId());
			payment.updateStatus(PaymentStatus.FAILED);
		} else {
			PaymentStatus paymentStatus = creditCardSimulationService.getWebhookStatus(creditCard.get().getNumber());
			payment.updateStatus(paymentStatus);
		}
	}

}