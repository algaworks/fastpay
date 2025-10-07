package com.algaworks.fastpay.domain.model.creditcard;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.OffsetDateTime;
import java.util.List;

public interface CreditCardRepository extends JpaRepository<CreditCard, String> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	void deleteAllByAssignmentExpiresAtLessThan(OffsetDateTime offsetDateTime);

	List<CreditCard> findAllByCustomerCode(String customerCode);
}
