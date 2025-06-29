package com.asv.hotel;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDateTime;


@OpenAPIDefinition(
		info = @Info(
				title = "Hotel API",
				version = "1.0",
				description = "API для управления"
		)
)

@SpringBootApplication
@EnableJpaRepositories(basePackages ="com.asv.hotel.repositories")

public class HotelApplication {

	public static void main(String[] args) {

		SpringApplication.run(HotelApplication.class, args);
		LocalDateTime localDateTime=LocalDateTime.now();
	}

}
