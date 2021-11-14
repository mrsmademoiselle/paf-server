package com.memorio.memorio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MemorioApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemorioApplication.class, args);
		System.out.println("Startup successful");
	}

}