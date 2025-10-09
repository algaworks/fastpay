package com.algaworks.fastpay.application.controller;

import com.algaworks.fastpay.application.config.FastPayProperties;
import com.algaworks.fastpay.application.exception.AccessDeniedOnResourceException;
import com.algaworks.fastpay.application.exception.BusinessException;
import com.algaworks.fastpay.application.model.*;
import com.algaworks.fastpay.application.service.CreditCardSimulationService;
import com.algaworks.fastpay.domain.model.creditcard.CreditCard;
import com.algaworks.fastpay.domain.model.creditcard.CreditCardRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class FastPayTokenizationController {

	private final CreditCardRepository creditCardRepository;
	private final FastPayProperties fastPayProperties;
	private final CreditCardSimulationService creditCardSimulationService;

	@PostMapping("/api/v1/public/tokenized-cards")
	@ResponseStatus(HttpStatus.CREATED)
	public TokenizedCardModel tokenize(@Valid @RequestBody CreditCartInput input,
									   @RequestHeader("Token") String pubToken) {

		if (!fastPayProperties.getPublicToken().equals(pubToken)) {
			throw new AccessDeniedOnResourceException("Use a valid private token.");
		}

		CreditCard creditCard = CreditCard.builder()
				.cvv(input.getCvv())
				.number(input.getNumber())
				.holderName(input.getHolderName())
				.holderDocument(input.getHolderDocument())
				.expYear(input.getExpYear())
				.expMonth(input.getExpMonth())
				.brand(creditCardSimulationService.getBrand(input.getNumber()))
				.assignmentExpiresAt(OffsetDateTime.now().plus(fastPayProperties.getTokenizedCardExpiresIn()))
				.build();

		if (creditCard.isCardExpired()) {
			throw new BusinessException("The credit card is expired.");
		}

		creditCardRepository.saveAndFlush(creditCard);

		return new TokenizedCardModel(creditCard.getToken(),
				creditCard.getAssignmentExpiresAt());
	}

}
