package se.emilkronholm.pastaserver;

import java.util.Arrays;
import java.util.List;

public class Request {

    boolean isOk;

    String protocol;
    String requestType;
    String path;
    String cookie;
    String userAgent;
    int contentLength;
    String contentType;
    String body;

    private Request() {
    }

    public static class HttpParser {

        static Request fromRawString(String requestRaw) {

            // Initialize our request (and start off by isOk = true)
            Request request = new Request();
            request.isOk = true;

            String[] requestSplit = requestRaw.split("\r\n");

            // First header protocol version, type and path. Example: "GET / HTTP/1.1" (Split by whitespace)
            {
                String[] firstLine = requestSplit[0].split(" ");
                if (firstLine.length < 3) {
                    request.isOk = false;
                    return request;
                }

                // Acceptable Request Type
                List<String> acceptableRequestTypes
                        = Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH", "HEAD", "OPTIONS", "TRACE", "CONNECT");

                // Retrieve 
                if (firstLine.length > 0 && acceptableRequestTypes.contains(firstLine[0].trim())) {
                    request.requestType = firstLine[0].trim();
                }

                // Path
                if (firstLine.length > 1 && firstLine[1].trim().matches("/[\\w\\-\\&\\?%_#=+./~]*")) {
                    request.path = firstLine[1];
                }

                // Protocol
                if (firstLine.length > 2 && firstLine[2].trim().matches("HTTP\\/[0-9]\\.[0-9]")) {
                    request.protocol = firstLine[2].trim();
                }
            }

            // Parse headers and body/payload
            // Initialize i=1 to skip firstline
            for (int i = 1; i < requestSplit.length; i++) {
                String currentHeader = requestSplit[i];

                // Special case: If we have an empty line, we are done parsing headers and it is time to parse body/payload
                if (currentHeader.equals("\r\n") || currentHeader.equals("\n") || currentHeader.equals("\r") || currentHeader.equals("")) {
                    // Extract payload
                    {
                        StringBuilder bodyBuilder = new StringBuilder();
                        for (int p = i + 1; p < requestSplit.length; p++) {
                            bodyBuilder.append(requestSplit[p]);
                        }
                        String body = bodyBuilder.toString();

                        if (!body.equals("")) {
                            request.body = body;
                        }

                        break;
                    }

                }

                // Extract key and value 
                {
                    // First check if currentHeader is correctly formated
                    if (!currentHeader.matches("[\\w-]+:.+")) {
                        // If current header isn't correctly formatted. We set isOk to false, but keep parsing. 
                        // This approach is debatable because 1. we need to mark a bad request 2. Should be forgiving overall
                        request.isOk = false;
                        continue;
                    }

                    String key = currentHeader.split(":")[0].trim();
                    String value = currentHeader.split(":")[1].trim();

                    switch (key) {
                        case "Cookie" ->
                            request.cookie = value;
                        case "User-Agent" ->
                            request.userAgent = value;
                        case "Content-Length" -> {
                            try {
                                request.contentLength = Integer.parseInt(value);
                            } catch (NumberFormatException e) {
                                request.isOk = false;
                            }
                        }
                        case "Content-Type" ->
                            request.contentType = value;
                    }
                }

            }
            return request;
        }

    }
}
