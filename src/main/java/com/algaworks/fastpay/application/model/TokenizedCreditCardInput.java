package com.algaworks.fastpay.application.model;

import lombok.Data;

@Data
public class TokenizedCreditCardInput {
	private String tokenizedCardId;
	private String customerCode;
}
