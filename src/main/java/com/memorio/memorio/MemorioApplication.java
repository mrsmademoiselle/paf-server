package com.memorio.memorio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.memorio.memorio.websocket.MemorioWebSocketServer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@SpringBootApplication
public class MemorioApplication {

    /* Exponiert alles was in /public/ drin ist nach aussen
    * Es stellt eine Alternative zum Endpunkt da, ueber dem Bilder abgefragt werden
    * und hat den Vorteil das Ressourcen direkt im fxml/jsx referenziert werden koennen */
    @Configuration
    @EnableWebMvc
    public class MvcConfig implements WebMvcConfigurer {
        // Uebeshreiben des Standartressourcehandlers: Standartmaessig werden noch andere Verzeichnisse nach aussen exponiert
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            // Mappen von allem was die /public/** URI beeinhaltet auf die entsprechende Ressource im /public/ Verzeichnis
            registry
                    .addResourceHandler("/public/**")
                    .addResourceLocations("classpath:/public/");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(MemorioApplication.class, args);

	// starte den WebSocketServer in einem neuen Thread.
	// TODO den WebSocketServer korrekt herunterfahren mit server.close() oder so...
	MemorioWebSocketServer server = MemorioWebSocketServer.getInstance();
	new Thread(() -> {server.start();}).start();
        System.out.println("Startup successful");
    }
}
