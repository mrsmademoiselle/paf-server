package com.example.javafx.service.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Klasse für Websocket-Kommunikation.
 * <p>
 * Erstmal als Singleton umgesetzt, weil wir aktuell immer auf denselben Port lauschen.
 */
public class SocketConnector {
    private static final SocketConnector instance = new SocketConnector();

    private SocketConnector() {
    }

    public boolean connect(String data) {
        try ( // Ressourcen-Try-Catch: Diese Ressourcen schließen automatisch,
              // egal, ob der try-Block normal durchläuft oder eine Exception wirft
              Socket socket = new Socket("localhost", 444);
              PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
              BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            // Daten rausschicken
            output.println(data);
            return readServerResponse(input);

        } catch (IOException e) {
            System.out.println("ioexception beim connecten:" + e.getMessage());
        }
        return false;
    }

    private boolean readServerResponse(BufferedReader input) {
        // Empfange Response vom Server
        String serverResponse = "";
        boolean found = false;

        /* die zweite Bedingung soll dem Abbruch dienen, funktioniert allerdings noch nicht ganz.
            Input so lange lesen, bis End-Trigger gefunden wurde.
         */
        while (!found && !Thread.currentThread().isInterrupted()) {

            try {
                serverResponse = input.readLine();
                // TODO: trigger anpassen
                if (serverResponse.equals("gefunden")) {
                    found = true;
                }
                System.out.println("Server response: " + serverResponse);
            } catch (IOException i) {
                break;
            }
        }
        return found;
    }

    public static SocketConnector getInstance() {
        return instance;
    }
}