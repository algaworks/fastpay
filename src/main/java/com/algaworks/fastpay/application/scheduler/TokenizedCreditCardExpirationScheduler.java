package com.algaworks.fastpay.application.scheduler;

import com.algaworks.fastpay.domain.model.creditcard.CreditCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class TokenizedCreditCardExpirationScheduler {

	private final CreditCardRepository creditCardRepository;

	@Scheduled(fixedRate = 120000L)
	@Transactional
	public void deleteExpired() {
		creditCardRepository.deleteAllByAssignmentExpiresAtLessThan(OffsetDateTime.now());
	}

}
