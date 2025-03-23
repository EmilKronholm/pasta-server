package se.emilkronholm.pastaserver;

import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionManager {

    // Accepts a socket-address and creates an event loop. 
    static void startServer(ServerSocket serverSocket) {
        try {
            while (true) {
                System.out.println("Waiting for client...");
                Socket client = serverSocket.accept(); // Blocking

                Thread.ofVirtual().start(() -> {
                    Connection.HandleConnection(client);
                });
            }
        } catch (Exception e) {
            System.out.println("Error in ConnectionManager event loop (startServer): " + e.toString());
        }

    }
}