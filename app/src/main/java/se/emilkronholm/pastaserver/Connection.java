package se.emilkronholm.pastaserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class Connection {
    
    // The plan is as follows:
    // 1. Parse the connection data
    // 2. Handle connection data (e.g. turning a request to a response)
    // 3. Write response
    static void HandleConnection(Socket client) {
        try {
            // We set timeout to 10000 ms to make sure no bad clients will hog a Connection forever
            client.setSoTimeout(10000);
        } catch (SocketException ex) {
            ex.printStackTrace();
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream())); PrintWriter out = new PrintWriter(client.getOutputStream(), true);) {

            // Step 1: Parse the client sent data
            String inputLine;
            String request = "";
            while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
                request += inputLine;
            }

            System.out.println("Request from client \n" + request);

            // Step 2: Get response from request
            String response = getResponse(request);

            // Step 3: Send the response
            out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static String getResponse(String request) {
        String body = """
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>Document</title></head>
<body><h1>Mock response</h1></body>
</html>
        """;

        String response = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "Content-Length: " + body.getBytes().length + "\r\n"
                + "\r\n"
                + body;

        return response;
    }
}
