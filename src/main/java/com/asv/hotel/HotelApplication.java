package com.asv.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableJpaRepositories(basePackages ="com.asv.hotel.repositories")
public class HotelApplication {

	public static void main(String[] args) {

		SpringApplication.run(HotelApplication.class, args);
		LocalDateTime localDateTime=LocalDateTime.now();
	}

}
