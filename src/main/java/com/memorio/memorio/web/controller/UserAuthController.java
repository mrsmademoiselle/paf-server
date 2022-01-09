package com.memorio.memorio.web.controller;


import com.memorio.memorio.services.UserAuthService;
import com.memorio.memorio.services.UserService;
import com.memorio.memorio.web.dto.JwtResponse;
import com.memorio.memorio.web.dto.UserAuthDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
/* Transactional zeigt an, dass jede aufgerufene Methode eine abgeschlossene Transaktion abbildet. In einer Transaktion
 übernimmt JPA bestimmte Operationen automatisch, z.B. das committen von Changes (Persistence Context) */
@Transactional
@RequestMapping("/user")
public class UserAuthController {
    private final UserService userService;
    private final UserAuthService userAuthService;
    private final Logger logger = LoggerFactory.getLogger(UserTestController.class);

    @Autowired
    public UserAuthController(UserService userService, UserAuthService userAuthService) {
        this.userService = userService;
        this.userAuthService = userAuthService;
    }

    /**
     * Registrierung
     * Wenn Username bereits vorhanden gebe 400 wenn User noch nicht vorhanden 200
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserAuthDto userAuthDto, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            logger.warn("{} hat das falsche Format. Fehler: {}", userAuthDto, bindingResult.getAllErrors());
            return new ResponseEntity<>("Benutzername oder Passwort sind nicht valide.", HttpStatus.BAD_REQUEST);
        }

        if (userService.saveUser(userAuthDto)) {
            logger.info("Registrierung war erfolgreich.");
            return loginUser(userAuthDto);
        } else {
            logger.warn("Benutzername ist bereits vergeben.");
            return new ResponseEntity<>("Der Benutzername ist bereits vergeben", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Login
     * wenn User gefunden werden kann und Zugangsdatem stimmen gebe Token sonst Exception mit 500
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserAuthDto userAuthDto) throws Exception {
        final String token = userAuthService.authenticateAndGetTokenForUserCredentials(userAuthDto.getUsername(), userAuthDto.getPassword());
        logger.info("Login erfolgreich.");
        return ResponseEntity.ok(new JwtResponse(token));
    }

    /**
     * "Pseudo"-Endpunkt für jene Client-Seiten, welche zwar einen Login erfordern, aber keinen eigenen Request an den
     * Server abfeuern müssen.
     * Dient der Validierung des JWT vor Ausgabe der Seite.
     * <p>
     * Habs erstmal im UserTestController gelassen, weil es entfernt etwas damit zutun hat. Können es aber auch gerne in einen
     * eigenen Controller auslagern.
     * <p>
     * Wegen unserer WebSecurityConfig wird bei allen Requests auf diesen Endpunkt automatisch das JWT validiert.
     * Erst wenn der Benutzer tatsächlich autorisiert ist, wird die Methode aufgerufen und es wird ein 200er zurückgegeben.
     * Ist der Benutzer nicht autorisiert (das Token nicht korrekt), wird ein 401 zurückgegeben.
     */
    @GetMapping("/check")
    public ResponseEntity<?> checkIfAuthorized() {
        logger.info("Benutzer ist autorisiert.");
        return ResponseEntity.ok("");
    }

}