package com.memorio.memorio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security gibt viele Sicherheitsmechanismen von Haus aus vor.
 * Eins davon ist die Absicherung vor unautorisierten HTTP-Requests durch Login.
 * Die Login Seite wird von Spring Security bereits mitgebracht und eingebaut,
 * die Konfiguration dafür findet hier statt.
 */
// Extenden ist notwendig um die HTTP-Konfigurationen anzupassen
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        // Generelle Rollenkonfigurationen
        auth.inMemoryAuthentication()
                .withUser("user").password(passwordEncoder().encode("password")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("password")).roles("ADMIN");
    }

    // Rollenkonfiguration für spezifische Seiten
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // Nur User kann auf /user zugreifen
                .antMatchers("/user", "/home").access("hasRole('USER')")
                .antMatchers("/admin/**").hasRole("ADMIN")
                // Alle Requests auf /allesErlaubt/** sind ohne Login erlaubt
                .antMatchers("/allesErlaubt/**").permitAll()
                .antMatchers("/h2/**").hasRole("ADMIN")
                .and()
                // Baut die Login-Form für die Authentifizierung
                .formLogin();
    }

    // Passwort Hashing wird von Spring übernommen
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}