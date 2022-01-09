package com.memorio.memorio.services;

import com.memorio.memorio.config.jwt.JwtTokenUtil;
import com.memorio.memorio.entities.User;
import com.memorio.memorio.exceptions.MemorioRuntimeException;
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
    private final Logger logger = LoggerFactory.getLogger(UserAuthService.class);

    public UserAuthService(JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager, UserRepository userRepository, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    /**
     * Versucht, den Benutzer zu authentifizieren. Gibt ein neues Token zurück, wenn erfolgreich.
     */
    public String authenticateAndGetTokenForUserCredentials(String username, String password) {
        // Pruefen ob Token existiert
        UserDetails userDetails = userService.loadUserByUsername(username);
        authenticate(username, password);

        return jwtTokenUtil.generateToken(userDetails);
    }

    /**
     * Versucht den Benutzer zu authentifizieren. Wirft eine Exception, wenn das nicht möglich ist.
     */
    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            logger.error("Account ist gesperrt. Darf in unserer Anwendung nicht auftreten.");
            throw new MemorioRuntimeException("Account ist gesperrt. Darf in unserer Anwendung nicht auftreten");
        } catch (BadCredentialsException e) {
            logger.error("Falsche Zugangsdaten");
            // Exception wenn der User nicht gefunden werden kann
            throw new MemorioRuntimeException("Falsche Zugangsdaten");
        }
    }

    /**
     * Gibt den User zu einem jwt zurück. Wenn dieser nicht existiert, wird eine NotFoundException geworfen.
     */
    public User getUserFromJwt(String jwtToken) {
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElseThrow(NotFoundException::new);
    }

    /**
     * Updated die Userinformationen des Users mit diesem Token. Gibt anschließend ein neues JWT zurück.
     */
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