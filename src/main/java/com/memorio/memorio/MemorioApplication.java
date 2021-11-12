package com.memorio.memorio;

import com.memorio.memorio.entities.User;

import com.memorio.memorio.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class MemorioApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemorioApplication.class, args);
	}

}