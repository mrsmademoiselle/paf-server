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

@Configuration
@SpringBootApplication
public class MemorioApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemorioApplication.class, args);
        createThread();
        System.out.println("Startup successful");
    }

    private static void createThread() {
        Thread server = new Thread(new Runnable() {

            @Override
            public void run() {
                // Ressourcen-Try-Catch: Diese Ressourcen schließen automatisch,
                // egal, ob der try-Block normal durchläuft oder eine Exception wirft
                try (
                        ServerSocket serverSocket = new ServerSocket(444);
                        Socket clientSocket = serverSocket.accept();
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Server: " + inputLine);
                        out.println("heard you");
                        if (inputLine.equals("bye")) {
                            System.out.println("Server: exiting");
                            break;
                        }
                        Thread.sleep(6000);
                        out.println("gefunden!!!!");
                        System.out.println("sent over");
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        server.setDaemon(true);
        server.start();
    }
}