package com.spring.batch.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.*"})
public class SpringBatchApplication {

	public static void main(String[] args) {
		//System.exit(SpringApplication.exit(SpringApplication.run(SpringBatchApplication.class, args)));
		SpringApplication.run(SpringBatchApplication.class, args);
	}

}
