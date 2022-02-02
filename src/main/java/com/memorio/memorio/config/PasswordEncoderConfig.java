package com.memorio.memorio.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PasswordEncoderConfig {
    // Diese Bean wird für die Erstellung des UserService-Objekts benötigt (PasswordEncoder)

    /**
     * Encoder fuer Passwoerter. Wird fuer das JWT-handling verwendet.
     * @return Bcrypt Encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
