package com.algaworks.fastpay.application.webhook;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class WebhookClient {

	public void send(PaymentWebhookEvent payment, String destination) {
		RestClient restClient = RestClient.create();

		ResponseEntity<Void> bodilessEntity = restClient.post()
				.uri(destination)
				.body(payment)
				.contentType(MediaType.APPLICATION_JSON)
				.retrieve().toBodilessEntity();

		if (!bodilessEntity.getStatusCode().is2xxSuccessful()) {
			throw new RuntimeException("Error on webhook, response code: " + bodilessEntity.getStatusCode());
		}
	}

}
