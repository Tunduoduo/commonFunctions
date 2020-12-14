package com.example.functions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * date
 * @author TIAN
 */
@EnableScheduling
@SpringBootApplication
public class FunctionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FunctionsApplication.class, args);
	}

}
