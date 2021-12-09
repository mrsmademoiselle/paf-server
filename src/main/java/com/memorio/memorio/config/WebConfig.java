package com.memorio.memorio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * "Custom" Konfigurationen für unsere Webanwendung, z.B. custom Cors-Mappings
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // für welche Controller-Pfade soll corsmapping gelten
        registry.addMapping("/user/**")
                .allowedOrigins("http://localhost:3000/");
        // hier sind noch weitere Konfigurationen möglich, z.B. .allowedMethods und .allowedHeaders,
        // die wir zum derzeitigen Zeitpunkt allerdings noch nicht brauchen
    }
}