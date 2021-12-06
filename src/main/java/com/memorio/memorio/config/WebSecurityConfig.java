package com.memorio.memorio.config;

import com.memorio.memorio.config.jwt.JwtAuthenticationExceptionHandler;
import com.memorio.memorio.config.jwt.JwtRequestFilter;
import com.memorio.memorio.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationExceptionHandler jwtAuthenticationExceptionHandler;
    private final UserService userService;
    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public WebSecurityConfig(JwtAuthenticationExceptionHandler jwtAuthenticationExceptionHandler, UserService userService, JwtRequestFilter jwtRequestFilter) {
        this.jwtAuthenticationExceptionHandler = jwtAuthenticationExceptionHandler;
        this.userService = userService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    // Diese Bean wird für die Erstellung des UserService-Objekts benötigt (PasswordEncoder)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // csrf aus machen, da nicht benoetigt bzw keine anforderung
        httpSecurity.csrf().disable()
                // auth-header check fuer folgende Endpunkte deaktivieren
                .authorizeRequests().antMatchers("/user/login", "/user/register").permitAll().
                // Alle Anfragen an anderen Endpunkten werden ueberprueft
                        anyRequest().authenticated().and().
                // Entrypoint fuer authmanagement angeben und Sessionmanagement deaktivieren
                        exceptionHandling().authenticationEntryPoint(jwtAuthenticationExceptionHandler).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Filter Richtlinie - jeder Header eines Requests wird einmalig nach dem Token validiert
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}