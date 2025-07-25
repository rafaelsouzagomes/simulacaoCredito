package com.interview.simuladorCredito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SimuladorCreditoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimuladorCreditoApplication.class, args);
	}

}
