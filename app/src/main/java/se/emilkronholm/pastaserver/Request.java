package se.emilkronholm.pastaserver;

import java.util.HashMap;
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

    private Request() {}

    public static class HttpParser {

        static Request fromRawString(String requestRaw) {
            Request request = new Request();
            Map<String, String> map = new HashMap();

            // TODO: Implement parsing algorithm

            return request;
        }
    }
}