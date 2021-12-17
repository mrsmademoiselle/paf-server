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

import javax.ws.rs.NotFoundException;
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


    public boolean saveUser(UserAuthDto user) {
        if (this.userRepository.existsByUsername(user.getUsername())) {
            return false;
        }
        try {
            // User generieren
            User newUser = new User(user.getUsername(), bcryptEncoder.encode(user.getPassword()));
            userRepository.save(newUser);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public User updateUser(String username, UserUpdateDto userUpdateDto) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.orElseThrow(NotFoundException::new);

        try {
            // franzi schickt leere Usernamen/passwörter mit, wenn die nicht gesetzt sind
            if (userUpdateDto.getUsername() != null && !userUpdateDto.getUsername().isBlank()) {
                user.setUsername(userUpdateDto.getUsername());
            }
            if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isBlank()) {
                user.setPassword(bcryptEncoder.encode(userUpdateDto.getPassword()));
            }
            if (userUpdateDto.getImg() != null && userUpdateDto.getImg().length > 0) {
                user.setImage(userUpdateDto.getImg());
            }
            user = userRepository.save(user);

            return user;
        } catch (Exception e) {
            return null;
        }
    }

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