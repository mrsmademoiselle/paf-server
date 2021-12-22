package com.example.javafx.service.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Singleton Klasse für Websocket-Kommunikation
 */
public class WebsocketConnector {
    private static final WebsocketConnector instance = new WebsocketConnector();

    private WebsocketConnector() {
    }

    public boolean connect(String data) {
        try ( // Ressourcen-Try-Catch: Diese Ressourcen schließen automatisch,
              // egal, ob der try-Block normal durchläuft oder eine Exception wirft
              Socket socket = new Socket("localhost", 444);
              PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
              BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            sendData(data, output);
            return readServerResponse(input);

        } catch (IOException e) {
            System.out.println("ioexception beim connecten:" + e.getMessage());
        }
        return false;
    }

    private void sendData(String data, PrintWriter output) {
        // Daten rausschicken
        output.println(data);
    }

    private boolean readServerResponse(BufferedReader input) {
        // Empfange Response vom Server
        String serverResponse = "";
        boolean found = false;

        while (!found && !Thread.currentThread().isInterrupted()) {
            System.out.println("loop");
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

    public static WebsocketConnector getInstance() {
        return instance;
    }
}