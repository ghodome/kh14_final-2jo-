package com.art;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ArtauctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtauctionApplication.class, args);
	}

}
