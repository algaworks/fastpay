package com.algaworks.fastpay.application.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreditCartInput {
	@NotBlank
	@Size(min = 8)
	private String number;

	@NotBlank
	@Size(min = 3, max = 4)
	private String cvv;

	@NotBlank
	private String holderName;

	@NotBlank
	private String holderDocument;

	@NotNull
	private Integer expMonth;

	@NotNull
	private Integer expYear;
}
