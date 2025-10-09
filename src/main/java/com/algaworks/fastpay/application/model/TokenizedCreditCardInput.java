package com.algaworks.fastpay.application.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenizedCreditCardInput {
	@NotBlank
	private String tokenizedCard;
	@NotBlank
	private String customerCode;
}
