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

    @Configuration
    @EnableWebMvc
    public class MvcConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
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
