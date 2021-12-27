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

@Configuration
@SpringBootApplication
public class MemorioApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemorioApplication.class, args);

	// starte den WebSocketServer in einem neuen Thread.
	// TODO den WebSocketServer korrekt herunterfahren mit server.close() oder so...
	MemorioWebSocketServer server = MemorioWebSocketServer.getInstance();
	new Thread(() -> {server.start();}).start();
        System.out.println("Startup successful");
    }
}
