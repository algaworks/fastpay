package com.algaworks.fastpay.application.controller;

import com.algaworks.fastpay.application.config.FastPayProperties;
import com.algaworks.fastpay.application.exception.AccessDeniedOnResourceException;
import com.algaworks.fastpay.application.exception.BusinessException;
import com.algaworks.fastpay.application.model.*;
import com.algaworks.fastpay.application.service.CreditCardSimulationService;
import com.algaworks.fastpay.domain.model.creditcard.CreditCard;
import com.algaworks.fastpay.domain.model.creditcard.CreditCardRepository;
import com.algaworks.fastpay.domain.model.payment.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class FastPayPaymentController {

	private final CreditCardRepository creditCardRepository;
	private final PaymentRepository paymentRepository;
	private final FastPayProperties fastPayProperties;
	private final CreditCardSimulationService creditCardSimulationService;

	@PostMapping("/api/v1/payments")
	@ResponseStatus(HttpStatus.CREATED)
	public PaymentModel capture(@RequestBody PaymentInput input, @RequestHeader("Token") String prvToken) {
		verifyPrivateToken(prvToken);
		Payment payment = paymentRepository.saveAndFlush(buildPayment(input));
		return PaymentModel.of(payment).build();
	}

	@GetMapping("/api/v1/payments/{paymentId}")
	public PaymentModel findById(@PathVariable String paymentId, @RequestHeader("Token") String prvToken) {
		verifyPrivateToken(prvToken);
		Payment payment = findPaymentById(paymentId);
		return PaymentModel.of(payment).build();
	}

	@PutMapping("/api/v1/payments/{paymentId}/refund")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void refund(@PathVariable String paymentId, @RequestHeader("Token") String prvToken) {
		verifyPrivateToken(prvToken);
		Payment payment = findPaymentById(paymentId);
		validateStatus(payment, PaymentStatus.PAID, "Payment is not paid, it cannot be refunded.");
		updatePaymentStatus(payment, PaymentStatus.REFUNDED);
	}

	@PutMapping("/api/v1/payments/{paymentId}/cancel")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancel(@PathVariable String paymentId, @RequestHeader("Token") String prvToken) {
		verifyPrivateToken(prvToken);
		Payment payment = findPaymentById(paymentId);
		validateStatus(payment, PaymentStatus.PENDING, String.format("Payment is not pending, it is %s, it cannot be cancelled.", payment.getStatus()));
		updatePaymentStatus(payment, PaymentStatus.CANCELLED);
	}

	private void verifyPrivateToken(String prvToken) {
		if (!fastPayProperties.getPrivateToken().equals(prvToken)) {
			throw new AccessDeniedOnResourceException("Use a valid private token.");
		}
	}

	private Payment buildPayment(PaymentInput input) {
		Payment.PaymentBuilder builder = input.toPayment();
		PaymentStatus status = PaymentStatus.PENDING;
		if (input.getMethod().equals(PaymentMethod.CREDIT)) {
			CreditCard creditCard = findCreditCard(input.getCreditCardId());
            status = creditCardSimulationService.getFirstStatus(creditCard.getNumber());
			builder.creditCardId(creditCard.getId());
		}
		Payment payment = builder.build();
		payment.updateStatus(status);
		return payment;
	}

	private CreditCard findCreditCard(String creditCardId) {
		return creditCardRepository.findById(creditCardId)
				.orElseThrow(() -> new BusinessException("Credit card not found."));
	}

	private Payment findPaymentById(String paymentId) {
		return paymentRepository.findById(paymentId)
				.orElseThrow(() -> new BusinessException("Payment not found."));
	}

	private void validateStatus(Payment payment, PaymentStatus expectedStatus, String errorMessage) {
		if (!payment.getStatus().equals(expectedStatus)) {
			throw new BusinessException(errorMessage);
		}
	}

	private void updatePaymentStatus(Payment payment, PaymentStatus newStatus) {
		payment.setStatus(newStatus);
		paymentRepository.saveAndFlush(payment);
	}

}