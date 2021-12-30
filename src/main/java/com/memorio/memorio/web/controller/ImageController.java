package com.memorio.memorio.web.controller;

import com.memorio.memorio.config.jwt.JwtTokenUtil;
import com.memorio.memorio.entities.User;
import com.memorio.memorio.repositories.UserRepository;
import com.memorio.memorio.services.UserService;
import com.memorio.memorio.web.dto.JwtResponse;
import com.memorio.memorio.web.dto.UserAuthDto;
import com.memorio.memorio.web.dto.UserDataResponse;
import com.memorio.memorio.web.dto.UserUpdateDto;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.NotFoundException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@RestController
@Transactional
@RequestMapping("/images")
public class ImageController {
    /**
     * Image Controller zum zur verfuegung stellen der Bilder
     */

    @Autowired
    public ImageController(){
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCardImage(@RequestHeader(name = "Cardname") String cardName)throws IOException {
        /**
         * Handeln der Kartenbilder TODO: Klaeren ob notwendig/mehrwert/gewollt oder doch vanilla spring weg
         */
        // Fuer Bilder aus /images/ - To Be Klaeren ob das ueberhaupt von nutzen ist, vgl log
        //var imgFile = new ClassPathResource("images/"+cardName);
        // Hat de gleichen effekt nur das im Header der Name der Ressource gesetzt werden muss
        var imgFile = new ClassPathResource("public/"+cardName);
        byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(bytes);
    }

}
