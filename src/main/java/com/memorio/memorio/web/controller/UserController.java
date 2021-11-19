package com.memorio.memorio.web.controller;

import com.memorio.memorio.entities.Match;
import com.memorio.memorio.entities.User;
import com.memorio.memorio.repositories.MatchRepository;
import com.memorio.memorio.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
/* Transactional zeigt an, dass jede aufgerufene Methode eine abgeschlossene Transaktion abbildet. In einer Transaktion
 übernimmt JPA bestimmte Operationen automatisch, z.B. das committen von Changes (Persistence Context) */
@Transactional
// localhost:9090/user
@RequestMapping("/quatsch")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    UserRepository userRepository;
    MatchRepository matchRepository;

    @Autowired
    public UserController(UserRepository userRepository, MatchRepository matchRepository) {
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("/match")
    public List<Match> getMatch() {
        return this.matchRepository.findAll();
    }

    @GetMapping("/user")
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    @GetMapping("/allesErlaubt")
    public String allesErlaubt() {
        return "Das hier sieht man auch ohne Login";
    }

    @GetMapping("/admin/**")
    public String nurAdmin() {
        return "Das hier sieht man nur als Admin";
    }
}