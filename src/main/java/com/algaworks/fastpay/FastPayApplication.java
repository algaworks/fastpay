package com.algaworks.fastpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FastPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FastPayApplication.class, args);
	}

}
