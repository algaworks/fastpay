package com.algaworks.fastpay.application.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties("fastpay")
public class FastPayProperties {

	@NotBlank
	private String publicToken;

	@NotBlank
	private String privateToken;

	@NotBlank
	private String hostname;

	@NotNull
	private Duration tokenizedCardExpiresIn;
}
