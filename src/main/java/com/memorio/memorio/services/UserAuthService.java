package com.memorio.memorio.services;

import com.memorio.memorio.config.jwt.JwtTokenUtil;
import com.memorio.memorio.entities.User;
import com.memorio.memorio.repositories.UserRepository;
import com.memorio.memorio.web.dto.UserUpdateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

@Service
public class UserAuthService {
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private Logger logger = LoggerFactory.getLogger(UserAuthService.class);

    public UserAuthService(JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager, UserRepository userRepository, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public String authenticateAndGetTokenForUserCredentials(String username, String password) throws Exception {
        // Pruefen ob Token existiert
        UserDetails userDetails = userService.loadUserByUsername(username);
        authenticate(username, password);

        return jwtTokenUtil.generateToken(userDetails);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            logger.error("Account ist gesperrt. Darf in unserer Anwendung nicht auftreten.");
            throw new Exception("", e);
        } catch (BadCredentialsException e) {
            logger.error("Falsche Zugangsdaten");
            // Exception wenn der User nicht gefunden werden kann
            throw new Exception("Falsche Zugangsdaten", e);
        }
    }

    public User getUserFromJwt(String jwtToken) {
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElseThrow(NotFoundException::new);
    }

    public String updateUser(UserUpdateDto userUpdateDto, String jwtToken) {
        // usernamen aus aktuellem Token holen
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        // user in der Datenbank updaten
        User user = userService.updateUser(username, userUpdateDto);

        // neues Token generieren
        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        return jwtTokenUtil.generateToken(userDetails);
    }
}