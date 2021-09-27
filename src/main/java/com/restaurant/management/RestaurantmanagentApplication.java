package com.restaurant.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class RestaurantmanagentApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantmanagentApplication.class, args);
	}

}
