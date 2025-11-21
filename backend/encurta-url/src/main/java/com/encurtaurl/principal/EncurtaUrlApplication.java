package com.encurtaurl.principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EncurtaUrlApplication {

	public static void main(String[] args) {
		SpringApplication.run(EncurtaUrlApplication.class, args);
	}

}
