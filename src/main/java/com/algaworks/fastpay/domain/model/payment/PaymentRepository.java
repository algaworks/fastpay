package com.algaworks.fastpay.domain.model.payment;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<Payment> findAllByNotifiedIsFalse(Limit limit);
}
