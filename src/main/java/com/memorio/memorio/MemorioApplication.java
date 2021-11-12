package com.memorio.memorio;

import com.memorio.memorio.entities.User;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MemorioApplication {

	public static void main(String[] args) {
	    	User user = new User();
		user.getId();
		SpringApplication.run(MemorioApplication.class, args);
	}

}
