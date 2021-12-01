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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import com.memorio.memorio.config.JwtTokenUtil;
import com.memorio.memorio.entities.JwtRequest;
import com.memorio.memorio.entities.JwtResponse;

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
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserController(UserRepository userRepository, MatchRepository matchRepository, UserService userService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
	    this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    @GetMapping("/users")
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    //Registrierung - Wenn Username bereits vorhanden gebe 400 wenn User noch nicht vorhanden 200
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserAuthDto userAuthDto) throws Exception {
        if(userService.saveUser(userAuthDto)){
            return ResponseEntity.ok("Registrierung erfolgreich");
        }else{
            return new ResponseEntity<>("Registrierung fehlgeschlagen - Benutzername bereits vergeben", HttpStatus.BAD_REQUEST);        }
    }

    //Login - wenn User gefunden werden kann und Zugangsdatem stimmen gebe Token sonst Exception mit 500
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody JwtRequest authenticationRequest)throws Exception{
        // Pruefen ob Token existiert
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        // Bauen des User-security-Objektes
        final UserDetails userDetails = userService
                .loadUserByUsername(authenticationRequest.getUsername());
        //Token generieren
        final String token = jwtTokenUtil.generateToken(userDetails);
        //Ausgabe des Tokens
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("", e);
        } catch (BadCredentialsException e) {
            //Exception wenn der User nicht gefunden werden kann
            throw new Exception("Falsche Zugangsdaten", e);
        }
    }
}
