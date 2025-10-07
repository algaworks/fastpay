package com.algaworks.fastpay.application.config;

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

	private String publicToken;

	private String privateToken;

	private String hostname;

	private Duration tokenizedCardExpiresIn;
}
