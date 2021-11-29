package com.memorio.memorio.web.controller;

import com.memorio.memorio.entities.Match;
import com.memorio.memorio.entities.User;
import com.memorio.memorio.repositories.MatchRepository;
import com.memorio.memorio.repositories.UserRepository;
import com.memorio.memorio.services.UserService;
import com.memorio.memorio.web.dto.UserAuthDto;
import com.memorio.memorio.entities.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties.MatchingStrategy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
/* Transactional zeigt an, dass jede aufgerufene Methode eine abgeschlossene Transaktion abbildet. In einer Transaktion
 übernimmt JPA bestimmte Operationen automatisch, z.B. das committen von Changes (Persistence Context) */
@Transactional
// localhost:9090/user
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    UserRepository userRepository;
    MatchRepository matchRepository;
    UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, MatchRepository matchRepository, UserService userService) {
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
	this.userService = userService;
    }

    @GetMapping("/match")
    public List<Match> getMatch() {
        return this.matchRepository.findAll();
    }

    @GetMapping("/users")
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

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register")
    public boolean registerUser(@RequestBody UserAuthDto userAuthDto) {
	return this.userService.saveUser(userAuthDto.getUsername(), userAuthDto.getPassword());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public String loginUser(@RequestBody UserAuthDto userAuthDto){

    /*
    * TODO: Return JWT Token AND Username + Userconfig to Client to persist information
    */
        return "Derp";
    }
}
