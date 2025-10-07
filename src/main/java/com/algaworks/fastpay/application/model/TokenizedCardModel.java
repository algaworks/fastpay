package com.algaworks.fastpay.application.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenizedCardModel {
	private String tokenizedCard;
	private OffsetDateTime expiresAt;
}
