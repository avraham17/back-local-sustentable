package edu.co.sena.worksite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WorksiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorksiteApplication.class, args);
	}

}