package se.emilkronholm.pastaserver;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class App {

    public String getGreeting() {
        return "Welcome to the pasta server!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());

        try {
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 3500);
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(address);
            ConnectionManager.startServer(serverSocket);
        } catch (Exception e) {
            System.out.println("Fatal error");
            System.out.println(e.getMessage());
        }

        System.out.println("Server is now closed.");
    }
}
