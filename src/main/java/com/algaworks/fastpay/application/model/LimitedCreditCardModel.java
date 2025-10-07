package com.algaworks.fastpay.application.model;

import com.algaworks.fastpay.domain.model.creditcard.CreditCard;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LimitedCreditCardModel {
	private String id;
	private String lastNumbers;
	private Integer expMonth;
	private Integer expYear;
	private String brand;

	public static LimitedCreditCardModel of(CreditCard creditCard) {
		return LimitedCreditCardModel.builder()
				.id(creditCard.getId())
				.lastNumbers(creditCard.getNumber().substring(creditCard.getNumber().length()-4))
				.expMonth(creditCard.getExpMonth())
				.expYear(creditCard.getExpYear())
				.brand(creditCard.getBrand())
				.build();
	}
}
