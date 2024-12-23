package com.uday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestapiApplication {

	public static void main(String[] args) {
		System.out.println("Hello Zombie");
		SpringApplication.run(RestapiApplication.class, args);
	}

}
