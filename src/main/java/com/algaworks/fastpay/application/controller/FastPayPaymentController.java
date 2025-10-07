package com.algaworks.fastpay.application.controller;

import com.algaworks.fastpay.application.config.FastPayProperties;
import com.algaworks.fastpay.application.exception.AccessDeniedOnResourceException;
import com.algaworks.fastpay.application.exception.BusinessException;
import com.algaworks.fastpay.application.model.*;
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

	@PostMapping("/api/v1/payments")
	@ResponseStatus(HttpStatus.CREATED)
	public PaymentModel capture(@RequestBody PaymentInput input,
								@RequestHeader("Token") String prvToken) {
		verifyToken(prvToken);

		Payment.PaymentBuilder paymentBuilder = input.toPayment();
		loadPaymentMethodInfo(input, paymentBuilder);
		addStatus(input, paymentBuilder);

		Payment payment = paymentRepository.saveAndFlush(paymentBuilder.build());

		return toModel(payment);
	}

	@GetMapping("/api/v1/payments/{paymentId}")
	public PaymentModel findById(@PathVariable String paymentId,
								 @RequestHeader("Token") String prvToken) {
		verifyToken(prvToken);

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new BusinessException("Payment not found."));

		return toModel(payment);
	}

	@PutMapping("/api/v1/payments/{paymentId}/refund")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void refund(@PathVariable String paymentId,
					   @RequestHeader("Token") String prvToken) {
		verifyToken(prvToken);

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new BusinessException("Payment not found."));

		if (!payment.getStatus().equals(PaymentStatus.PAID)) {
			throw new BusinessException("Payment is not paid, it cannot be refunded.");
		}

		payment.setStatus(PaymentStatus.REFUNDED);

		paymentRepository.saveAndFlush(payment);
	}

	@PutMapping("/api/v1/payments/{paymentId}/cancel")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancel(@PathVariable String paymentId,
					   @RequestHeader("Token") String prvToken) {
		verifyToken(prvToken);

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new BusinessException("Payment not found."));

		if (!payment.getStatus().equals(PaymentStatus.PENDING)) {
			throw new BusinessException(String.format("Payment is not pending, it is %s, it cannot be cancelled.",
					payment.getStatus()));
		}

		payment.setStatus(PaymentStatus.CANCELLED);

		paymentRepository.saveAndFlush(payment);
	}

	private void verifyToken(String prvToken) {
		if (!this.fastPayProperties.getPrivateToken().equals(prvToken)) {
			throw new AccessDeniedOnResourceException("Use a valid private token.");
		}
	}

	private void addStatus(PaymentInput input, Payment.PaymentBuilder paymentBuilder) {
		paymentBuilder.status(PaymentStatus.PROCESSING); //todo if com base no cartao
	}

	private PaymentModel toModel(Payment payment) {
		return PaymentModel.build(payment).build();
	}

	private void loadPaymentMethodInfo(PaymentInput input, Payment.PaymentBuilder paymentBuilder) {
		if (input.getMethod().equals(PaymentMethod.CREDIT)) {
			loadCardInfo(input, paymentBuilder);
		}
	}

	private void loadCardInfo(PaymentInput input, Payment.PaymentBuilder paymentBuilder) {
		if (input.getTokenizedCardId() != null) {
			loadTockenizedCard(input, paymentBuilder);
		} else {
			throw new BusinessException("Card payment method requires a tokenized card");
		}

	}

	private void loadTockenizedCard(PaymentInput input, Payment.PaymentBuilder paymentBuilder) {
		CreditCard creditCard = creditCardRepository.findById(input.getTokenizedCardId())
				.orElseThrow(() -> new BusinessException("Credit card not found."));
		if (creditCard.isCardExpired()) {
			throw new BusinessException("Credit card is expired.");
		}
		paymentBuilder.tokenizedCreditCardId(creditCard.getId());
	}

}