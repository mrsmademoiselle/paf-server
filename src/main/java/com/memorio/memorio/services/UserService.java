package com.memorio.memorio.services;

import com.memorio.memorio.entities.User;
import com.memorio.memorio.repositories.UserRepository;
import com.memorio.memorio.web.dto.UserAuthDto;
import com.memorio.memorio.web.dto.UserUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.ws.rs.NotFoundException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    /*
    Dieses Feld muss field injection haben, damit kein Zyklus entsteht. Bei uns besteht der Zyklus bei PasswordEncoder,
    weil Spring nicht entscheiden kann, welche Bean zuerst erstellt werden soll.

    Begründung:
    Spring versucht zu Beginn die WebSecurityConfig zu erstellen.
    Dafür braucht er eine UserService-Instanz (Konstruktor). Um die zu erstellen, braucht er aber eine Bean von
    PasswordEncoder, die erst später in der WebSecurityConfig erstellt wird.
    Somit kann Spring nicht entscheiden, in welcher Reihenfolge die Dependencies aufgelöst werden sollen, weil ein
    Zyklus besteht: WebSecurity braucht UserService, UserService braucht PasswordEncoder, PasswordEncoder
    braucht zu seiner eigenen Erstellung die fertige WebSecurity.

    Warum Field Injection hier funktioniert:
    Field Injections finden (anders als Constructor Injections) erst dann statt, wenn sie gebraucht werden, sodass
    zu diesem Zeitpunkt die PasswordEncoder-Bean bereits existiert, weil der Konstruktor von UserService bereits erfolgreich
    aufgerufen wurde.

    Weitere Workarounds wären z.B. das Auslagern der PasswordEncoder-Bean in eine andere Klasse oder das field
    injecten des UserServices in der WebsecurityConfig.
    */
    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Persistiert User
     * @param user Zu speichernden User
     * @return true wenn Objekt persistiert, false wenn Exception eintritt
     */
    public boolean saveUser(UserAuthDto user) {
        if (this.userRepository.existsByUsername(user.getUsername())) {
            return false;
        }
        try {
            // User generieren
            User newUser = new User(user.getUsername(), bcryptEncoder.encode(user.getPassword()));
            userRepository.save(newUser);
            // Bild setzen
            URL url = Thread.currentThread().getContextClassLoader().getResource("images/default.jpg");
            try {
                // Standardbild "lesen" und im aktuellen User setzen, auch wenn er ein eigenes Bild hochladen wird
                // Persistieren wir praeventiv schonmal das Defaultbild
                BufferedImage bufferImage = ImageIO.read(url);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                ImageIO.write(bufferImage, "jpg", output);
                byte[] data = output.toByteArray();
                this.saveUserImage(user.getUsername(), data);
            } catch (IOException e) {
                System.out.println("Problem beim laden des Defaultbildes!");
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * User aktualisieren
     * @param username Name des zu aktualisierenden Users
     * @param userUpdateDto Neues Objekt mit Infos die uebertragen werden sollen
     * @return User wenn aktualisieren erfolgreich, Null wenn nichts geschehen
     */
    public User updateUser(String username, UserUpdateDto userUpdateDto) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.orElseThrow(NotFoundException::new);

        try {
            // usernamen aktualisieren
            if (userUpdateDto.getUsername() != null && !userUpdateDto.getUsername().isBlank()) {
                user.setUsername(userUpdateDto.getUsername());
            }
            // PW aktualisieren
            if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isBlank()) {
                // Vor dem aktualisieren des Passworts muss es noch durch den bcryptEncoder verarbeitet werden fuer JWT
                user.setPassword(bcryptEncoder.encode(userUpdateDto.getPassword()));
            }
            // Bild aktualisieren
            if (userUpdateDto.getImg() != null && userUpdateDto.getImg().length > 0) {
                user.setImage(userUpdateDto.getImg());
            }
            user = userRepository.save(user);

            return user;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Persistieren des Benutzerbildes
     * @param username Username des Users dessen Bild aktualisiert werden soll
     * @param userImage Bild als Byte-Array
     * @return true wenn neues Bild gesetzt wurde, false wenn es Probleme gab
     */
    public boolean saveUserImage(String username, byte[] userImage) {
        if (!this.userRepository.existsByUsername(username)) {
            return false;
        }
        try {
            Optional<User> userOptional = userRepository.findByUsername(username);
            User user = userOptional.orElseThrow(NotFoundException::new);
            user.setImage(userImage);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Laden von Benutzerdetails
     * @param username Username des users
     * @return Userdetails
     * @throws UsernameNotFoundException Exceptionhandling
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optional = userRepository.findByUsername(username);

        if (optional.isEmpty())
            throw new UsernameNotFoundException("Kein Benutzer mit dem Namen " + username + " gefunden");

        User user = optional.get();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }

}