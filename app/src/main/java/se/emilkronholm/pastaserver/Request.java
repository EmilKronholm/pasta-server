package se.emilkronholm.pastaserver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

    boolean isOk;

    String protocol;
    String requestType;
    String route;
    String cookie;
    String userAgent;
    int contentLength;
    String contentType;
    String body;

    private Request() {
    }

    public static class HttpParser {

        static Request fromRawString(String requestRaw) {
            Request request = new Request();
            Map<String, String> map = new HashMap();

            // TODO: Implement parsing algorithm
            String[] strs = requestRaw.split("\n");

            // First header protocol version, type and router
            String[] first = strs[0].split(" ");

            System.out.println(Arrays.toString(first));

            if (first.length < 3) {
                request.isOk = false;
                return request;
            }

            // Request Type
            List<String> acceptableRequestTypes
                    = Arrays.asList("GET", "POST", "DELTE", "PUT", "PATCH", "HEAD", "OPTIONS", "TRACE", "CONNECT");

            if (first.length > 0 && acceptableRequestTypes.contains(first[0].trim())) {
                request.requestType = first[0].trim();
            }

            // Route
            if (first.length > 1 && first[1].trim().matches("/[\\w\\-\\&\\?%_#=+./~]*")) {
                request.route = first[1];
            }

            // Protocol
            if (first.length > 2 && first[2].trim().matches("HTTP\\/[0-9]\\.[0-9]")) {
                request.protocol = first[2].trim();
            }

            System.out.println(request.requestType);
            System.out.println(request.route);
            System.out.println(request.protocol);

            // Initialize i=1 to skip headers
            for (int i = 1; i < strs.length; i++) {
                String str = strs[i];

                // Time to parse payload
                if (str.equals("\r\n") || str.equals("\n") || str.equals("\r") || str.equals("")) {
                    String body = "";
                    for (int p = i + 1; p < strs.length; p++) {
                        if (!strs[p].equals("\r")) {
                            body += strs[p];
                        }
                    }
                    if (!body.equals("")) {
                        request.body = body;
                    }

                    break;
                }

                System.err.println(str);
                System.err.println(Arrays.toString(str.split(":")));

                String key = str.split(":")[0].trim();
                String value = str.split(":")[1].trim();

                switch (key) {
                    case "Cookie" -> request.cookie = value;
                    case "User-Agent" -> request.userAgent = value;
                    case "Content-Length" -> request.contentLength = Integer.parseInt(value);
                    case "Content-Type" -> request.contentType = value;
                }
            }

            request.isOk = true;

            System.out.println(request.contentType);
            System.out.println(request.contentLength);
            System.out.println(request.cookie);
            System.out.println(request.userAgent);
            System.out.println(request.isOk);
            System.out.println(request.body);

            return request;
        }

    }
}
