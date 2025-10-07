package com.algaworks.fastpay.application.controller;

import com.algaworks.fastpay.application.config.FastPayProperties;
import com.algaworks.fastpay.application.exception.AccessDeniedOnResourceException;
import com.algaworks.fastpay.application.exception.BusinessException;
import com.algaworks.fastpay.application.exception.CreditCardAlreadyAssignedCustomerException;
import com.algaworks.fastpay.application.model.LimitedCreditCardModel;
import com.algaworks.fastpay.application.model.TokenizedCreditCardInput;
import com.algaworks.fastpay.domain.model.creditcard.CreditCard;
import com.algaworks.fastpay.domain.model.creditcard.CreditCardRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Transactional
@CrossOrigin(origins = "*", maxAge = 3600)
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

        CreditCard creditCard = creditCardRepository.findById(input.getTokenizedCardId()).orElseThrow();

        if (creditCard.isAssigned()) {
            throw new CreditCardAlreadyAssignedCustomerException(
                    "Tokenized card is already assigned to a customer.", input.getTokenizedCardId());
        }

        if (creditCard.isAssignmentExpired()) {
            //Expired unassigned cards will be deleted automatically
            throw new BusinessException("Tokenized card is expired.");
        }

        creditCard.setCustomerCode(input.getCustomerCode());
        creditCard.removeExpiration();

        creditCardRepository.saveAndFlush(creditCard);

        return LimitedCreditCardModel.of(creditCard);
    }

    @GetMapping("/api/v1/credit-cards")
    public List<LimitedCreditCardModel> findAllByCustomer(@RequestParam String customerCode) {
        return creditCardRepository.findAllByCustomerCode(customerCode)
                .stream().map(LimitedCreditCardModel::of).toList();
    }

}
