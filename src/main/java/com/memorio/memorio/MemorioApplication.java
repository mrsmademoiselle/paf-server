package com.memorio.memorio;

import com.memorio.memorio.entities.*;
import com.memorio.memorio.repositories.CardRepository;
import com.memorio.memorio.repositories.CardSetRepository;
import com.memorio.memorio.repositories.MatchRepository;
import com.memorio.memorio.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class MemorioApplication implements CommandLineRunner {

	private UserRepository userRepository;
	private CardRepository cardRepository;
	private CardSetRepository cardSetRepository;
	private MatchRepository matchRepository;

	@Autowired
	public MemorioApplication(UserRepository userRepository, CardRepository cardRepository, CardSetRepository cardSetRepository, MatchRepository matchRepository) {
		this.userRepository = userRepository;
		this.cardRepository = cardRepository;
		this.cardSetRepository = cardSetRepository;
		this.matchRepository = matchRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(MemorioApplication.class, args);
		System.out.println("Startup successful");
	}

	@Override
	public void run(String... args) throws Exception {
		User lobbyAdmin = new User("first1", "pw1");
		this.userRepository.save(lobbyAdmin);
		this.userRepository.save(new User("first2", "pw2"));
		this.userRepository.save(new User("first3", "pw3"));
		this.userRepository.save(new User("first4", "pw4"));

		Card card = new Card("path");
		this.cardRepository.save(card);
		CardSet cardSet = new CardSet("name", Arrays.asList(card));
		this.cardSetRepository.save(cardSet);
		this.matchRepository.save(new Match(123L, lobbyAdmin, new Board(12, cardSet)));
	}

}