package com.memorio.memorio.web.controller;


import com.memorio.memorio.entities.User;
import com.memorio.memorio.services.UserAuthService;
import com.memorio.memorio.services.UserService;
import com.memorio.memorio.web.dto.TokenDto;
import com.memorio.memorio.web.dto.UserInfoDto;
import com.memorio.memorio.web.dto.UserUpdateDto;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

@RestController
/* Transactional zeigt an, dass jede aufgerufene Methode eine abgeschlossene Transaktion abbildet. In einer Transaktion
 übernimmt JPA bestimmte Operationen automatisch, z.B. das committen von Changes (Persistence Context) */
@Transactional
@RequestMapping("/user")
public class UserInformationController {
    private final UserService userService;
    private final UserAuthService userAuthService;

    private final Logger logger = LoggerFactory.getLogger(UserInformationController.class);

    @Autowired
    public UserInformationController(UserService userService, UserAuthService userAuthService) {
        this.userService = userService;
        this.userAuthService = userAuthService;
    }

    /**
     * Info-Endpunkt fuer Benutzerprofil
     */
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader(name = "Authorization") String jwtToken) {
        User user = userAuthService.getUserFromJwt(jwtToken);

        byte[] img = user.getImage() == null ? getDefaultImg() : user.getImage();
        // für den FX-Client müssen wir das Bild einmal encoden, da wird es dort dann leichter parsen können
        String encodedString = java.util.Base64.getEncoder().encodeToString(img);

        return ResponseEntity.ok(new UserInfoDto(user.getUsername(), encodedString, img));
    }

    /**
     * Image-Endpunkt fuer das Bild
     */
    @GetMapping("/info/image")
    public ResponseEntity<?> getUserImage(@RequestHeader(name = "Authorization") String jwtToken) {
        User user = userAuthService.getUserFromJwt(jwtToken);

        byte[] image = user.getImage();
        if (image == null) {
            return ResponseEntity.ok(getDefaultImg());
        }
        return ResponseEntity.ok(image);
    }

    /**
     * Update-Endpunkt fuer das Benutzerprofil, wenn das Profil aktualisiert wird
     */
    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto, BindingResult bindingResult, @RequestHeader(name = "Authorization") String jwtToken) throws Exception {
        if (bindingResult.hasErrors()) {
            logger.warn("Der Username hat das falsche Format.");
            return new ResponseEntity<>("Der Username entspricht nicht dem richtigen Format.", HttpStatus.BAD_REQUEST);
        }
        String token = userAuthService.updateUser(userUpdateDto, jwtToken);
        return ResponseEntity.ok(new TokenDto(token));
    }

    /**
     * Bild-Hochlade Endpunkt
     */
    @PostMapping("/image/upload")
    public ResponseEntity<?> uploadImage(@RequestBody byte[] decodedBytes, @RequestHeader(name = "Authorization") String jwtToken) {
        // notwendig für JavaFX, weil Jackson byte[] automatisch mit byte64 encoded.
        // Evtl React anpassen.
        byte[] profilePicBytes = Base64.decodeBase64(decodedBytes);

        try {
            User user = userAuthService.getUserFromJwt(jwtToken);
            userService.saveUserImage(user.getUsername(), profilePicBytes);
            // User muss wegen @Transactional nicht händisch persistiert werden
            logger.info("Profilbild wurde erfolgreich im User {} gespeichert", user.getUsername());
        } catch (Exception e) {
            logger.error("Profilbild konnte nicht gespeichert werden: {}", e.getMessage());
            return new ResponseEntity<>("Das Profilbild konnte nicht gespeichert werden.", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("");
    }

    /**
     * Entfernt das Bild und setzt ueber den Server das Defaultbild
     */
    @GetMapping("/image/remove")
    public ResponseEntity<?> removeImage(@RequestHeader(name = "Authorization") String jwtToken) {
        User user = userAuthService.getUserFromJwt(jwtToken);

        // switch to default image
        userService.saveUserImage(user.getUsername(), getDefaultImg());
        String encodedString = java.util.Base64.getEncoder().encodeToString(getDefaultImg());

        return ResponseEntity.ok(new UserInfoDto(user.getUsername(), encodedString, getDefaultImg()));
    }

    /**
     * Laedt Default Bild aus Datei damit es spaeter persistiert und an die Clients vermittelt werden kann
     */
    private byte[] getDefaultImg() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("images/default.jpg");
        try {
            BufferedImage bufferImage = ImageIO.read(url);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(bufferImage, "jpg", output);
            byte[] data = output.toByteArray();
            return data;
        } catch (IOException e) {
            logger.error("Could not read default profile image");
            return null;
        }
    }

}