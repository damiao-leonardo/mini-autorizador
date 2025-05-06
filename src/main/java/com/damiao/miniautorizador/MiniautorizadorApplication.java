package com.damiao.miniautorizador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class MiniautorizadorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniautorizadorApplication.class, args);
	}

}
