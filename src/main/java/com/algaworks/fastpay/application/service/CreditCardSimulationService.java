package com.algaworks.fastpay.application.service;

import com.algaworks.fastpay.domain.model.payment.PaymentStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditCardSimulationService {

    private record CardSimulation(
            String cardNumber,
            String brand,
            PaymentStatus firstStatus,
            PaymentStatus webhookStatus
    ) {
    }

    private static final List<CardSimulation> SIMULATIONS = List.of(
            new CardSimulation("5120350100064537", "Master", PaymentStatus.PENDING, PaymentStatus.PROCESSING),
            new CardSimulation("5120350100064545", "Master", PaymentStatus.PROCESSING, PaymentStatus.PAID),
            new CardSimulation("5120350100064552", "Master", PaymentStatus.PROCESSING, PaymentStatus.FAILED),
            new CardSimulation("5120350100064560", "Master", PaymentStatus.PAID, PaymentStatus.REFUNDED),
            new CardSimulation("4622943127011022", "Visa", PaymentStatus.PAID, null), // Sem Webhook Status
            new CardSimulation("4622943127011030", "Visa", PaymentStatus.FAILED, null), // Sem Webhook Status
            new CardSimulation("4622943127011048", "Visa", PaymentStatus.PENDING, null), // Sem Webhook Status
            new CardSimulation("4622943127011055", "Visa", PaymentStatus.REFUNDED, null) // Sem Webhook Status
    );

    public Optional<CardSimulation> findSimulation(String creditCardNumber) {
        return SIMULATIONS.stream()
                .filter(sim -> sim.cardNumber().equals(creditCardNumber))
                .findFirst();
    }

    public PaymentStatus getFirstStatus(String creditCardNumber) {
        return findSimulation(creditCardNumber)
                .map(CardSimulation::firstStatus)
                .orElse(PaymentStatus.FAILED);
    }

    public Optional<PaymentStatus> getWebhookStatus(String creditCardNumber) {
        return findSimulation(creditCardNumber)
                .map(cardSimulation -> {
                    if (cardSimulation.webhookStatus() != null) {
                        return cardSimulation.webhookStatus();
                    }
                    return cardSimulation.firstStatus();
                });
    }

    public String getBrand(String creditCardNumber) {
        return findSimulation(creditCardNumber)
                .map(CardSimulation::brand)
                .orElse("UNKNOWN");
    }
}