package com.memorio.memorio.web.controller;

import com.memorio.memorio.config.jwt.JwtTokenUtil;
import com.memorio.memorio.entities.User;
import com.memorio.memorio.repositories.UserRepository;
import com.memorio.memorio.services.UserService;
import com.memorio.memorio.web.dto.JwtResponse;
import com.memorio.memorio.web.dto.UserAuthDto;
import com.memorio.memorio.web.dto.UserDataResponse;
import com.memorio.memorio.web.dto.UserUpdateDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.nio.file.Files;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;


@RestController
/* Transactional zeigt an, dass jede aufgerufene Methode eine abgeschlossene Transaktion abbildet. In einer Transaktion
 übernimmt JPA bestimmte Operationen automatisch, z.B. das committen von Changes (Persistence Context) */
@Transactional
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserRepository userRepository, UserService userService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/all")
    public List<User> getUsers() {
        return this.userRepository.findAll();
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
        final String token = getTokenForUser(userAuthDto);
        logger.info("Login erfolgreich.");
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto, BindingResult bindingResult, @RequestHeader(name="Authorization")String jwtToken) throws Exception {
	// find Username
	String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
	System.out.println(username);
	// get Entity
	try {
	    User user = userService.updateUser(username, userUpdateDto);
	    // create UserDetails
	    UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
	    // generate new Token
	    UserAuthDto uad = new UserAuthDto(user.getUsername(), user.getPassword());
	    return loginUser(uad);
	    //return ResponseEntity.ok(new JwtResponse(token));
	} catch(Exception e){
	    e.printStackTrace();
            return new ResponseEntity<>("Der Benutzername ist bereits vergeben", HttpStatus.BAD_REQUEST);
	}
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader(name = "Authorization") String jwtToken){
	String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.orElseThrow(NotFoundException::new);
	byte []	image = user.getImage();
	return ResponseEntity.ok(new UserDataResponse(username, image));
    }

    // Falls im Request anderes Format, evtl MultipartFile, Blob oder einfach InputStream statt byte[]
    // -> abzusprechen
    @PostMapping("/image/upload")
    public ResponseEntity<?> uploadImage(@RequestBody byte[] profilePicBytes, @RequestHeader(name = "Authorization") String jwtToken) {
        try {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            Optional<User> userOptional = userRepository.findByUsername(username);
            // zu dieser Exception darf es eigentlich nie kommen, aber besser haben als nicht haben
            User user = userOptional.orElseThrow(NotFoundException::new);
	    this.setProfileImg(user, profilePicBytes);
            // User muss wegen @Transactional nicht händisch persistiert werden
            logger.info("Profilbild wurde erfolgreich im User {} gespeichert", user.getUsername());
        } catch (Exception e) {
            logger.error("Profilbild konnte nicht gespeichert werden: {}", e.getMessage());
            return new ResponseEntity<>("Das Profilbild konnte nicht gespeichert werden.", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("");
    }

    @GetMapping("/image/remove")
    public ResponseEntity<?> removeImage(@RequestHeader(name = "Authorization") String jwtToken) {
	String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
	Optional<User> userOptional = userRepository.findByUsername(username);
	User user = userOptional.orElseThrow(NotFoundException::new);
	// switch to default image
	this.setProfileImg(user, null);
	return ResponseEntity.ok(new UserDataResponse(username, getDefaultImg()));
    }

    /**
     * "Pseudo"-Endpunkt für jene Client-Seiten, welche zwar einen Login erfordern, aber keinen eigenen Request an den
     * Server abfeuern müssen.
     * Dient der Validierung des JWT vor Ausgabe der Seite.
     * <p>
     * Habs erstmal im UserController gelassen, weil es entfernt etwas damit zutun hat. Können es aber auch gerne in einen
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

    private void authenticate(String username, String password) throws Exception {
        try {
	    System.out.println(username + "   " + password);
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

    private String getTokenForUser(UserAuthDto dto) throws Exception {
        // Pruefen ob Token existiert
        authenticate(dto.getUsername(), dto.getPassword());
        UserDetails userDetails = userService.loadUserByUsername(dto.getUsername());
        return jwtTokenUtil.generateToken(userDetails);
    }

    private void setProfileImg(User user, byte [] imgArray){
	if(imgArray == null){imgArray = this.getDefaultImg();}
	user.setImage(imgArray);
    }

    private byte[] getDefaultImg(){
	URL url = Thread.currentThread().getContextClassLoader().getResource("images/default.jpg");
	try {
	    BufferedImage bufferImage = ImageIO.read(url);
	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    ImageIO.write(bufferImage, "jpg", output);
	    byte [] data = output.toByteArray();
	    return data;
	} catch (IOException e) {
	    logger.error("Could not read default profile image");
	    return null;
	}
    }
}