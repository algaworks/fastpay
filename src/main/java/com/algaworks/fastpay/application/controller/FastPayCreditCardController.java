package com.algaworks.fastpay.application.controller;

import com.algaworks.fastpay.application.config.FastPayProperties;
import com.algaworks.fastpay.application.exception.AccessDeniedOnResourceException;
import com.algaworks.fastpay.application.exception.BusinessException;
import com.algaworks.fastpay.application.exception.CreditCardAlreadyAssignedCustomerException;
import com.algaworks.fastpay.application.exception.DomainEntityNotFound;
import com.algaworks.fastpay.application.model.LimitedCreditCardModel;
import com.algaworks.fastpay.application.model.TokenizedCreditCardInput;
import com.algaworks.fastpay.domain.model.creditcard.CreditCard;
import com.algaworks.fastpay.domain.model.creditcard.CreditCardRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class FastPayCreditCardController {

    private final CreditCardRepository creditCardRepository;
    private final FastPayProperties fastPayProperties;

    @PostMapping("/api/v1/credit-cards")
    @ResponseStatus(HttpStatus.CREATED)
    public LimitedCreditCardModel createCreditCard(@Valid @RequestBody TokenizedCreditCardInput input,
                                                   @RequestHeader("Token") String privateToken) {

        if (!fastPayProperties.getPrivateToken().equals(privateToken)) {
            throw new AccessDeniedOnResourceException("Use a valid private token.");
        }

        CreditCard creditCard = creditCardRepository.findByToken(input.getTokenizedCard())
                .orElseThrow(() -> new BusinessException("Tokenized card not found."));

        if (creditCard.isAssigned()) {
            throw new CreditCardAlreadyAssignedCustomerException(
                    "Tokenized card is already assigned to a customer.", input.getTokenizedCard());
        }

        creditCard.assign(input.getCustomerCode());

        creditCardRepository.saveAndFlush(creditCard);

        return LimitedCreditCardModel.of(creditCard);
    }

    @GetMapping("/api/v1/credit-cards")
    public List<LimitedCreditCardModel> findAllByCustomer(@RequestParam String customerCode) {
        return creditCardRepository.findAllByCustomerCode(customerCode)
                .stream().map(LimitedCreditCardModel::of).toList();
    }

    @DeleteMapping("/api/v1/credit-cards/{creditCardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@RequestParam String creditCardId) {
        creditCardRepository.deleteById(creditCardId);
    }

    @GetMapping("/api/v1/credit-cards/{creditCardId}")
    public LimitedCreditCardModel getById(@RequestParam String creditCardId) {
        CreditCard creditCard = creditCardRepository.findById(creditCardId)
                .orElseThrow(()-> new DomainEntityNotFound(String.format("Credit card %s not found.", creditCardId)));
        return LimitedCreditCardModel.of(creditCard);
    }

}
