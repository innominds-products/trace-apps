package com.inm.tracewise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TraceWiseApplication {
	public static void main(String[] args) {
		SpringApplication.run(TraceWiseApplication.class, args);
	}
}
