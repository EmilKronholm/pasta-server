package se.emilkronholm.pastaserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class RequestTest {

    @Test
    void httpParserTestWithMinimalRequest() {
        String requestRaw = "GET / HTTP/1.1\r\n\r\n";
        Request request = Request.HttpParser.fromRawString(requestRaw);

        assertEquals("HTTP/1.1", request.protocol);
        assertEquals("GET", request.requestType);
        assertEquals("/", request.path);

        assertTrue(request.isOk);
        assertEquals(0, request.contentLength);

        assertNull(request.contentType);
        assertNull(request.cookie);
        assertNull(request.userAgent);
        assertNull(request.body);
    }

    @Test
    void httpParserTestWithComplexValidRequest() {
        String requestRaw = 
            "POST /login HTTP/1.1\r\n" +
            "Host: example.com\r\n" +
            "User-Agent: JavaTest\r\n" +
            "Cookie: session=abc123\r\n" +
            "Content-Type: application/json\r\n" +
            "Content-Length: 27\r\n\r\n" +
            "{\"username\":\"emil\",\"pw\":\"x\"}";

        Request request = Request.HttpParser.fromRawString(requestRaw);

        assertEquals("HTTP/1.1", request.protocol);
        assertEquals("POST", request.requestType);
        assertEquals("/login", request.path);
        assertEquals("application/json", request.contentType);
        assertEquals("session=abc123", request.cookie);
        assertEquals("JavaTest", request.userAgent);
        assertEquals(27, request.contentLength);
        assertEquals("{\"username\":\"emil\",\"pw\":\"x\"}", request.body);

        assertTrue(request.isOk);
    }

    @Test
    void httpParserTestMissingContentLength() {
        String requestRaw =
            "POST /submit HTTP/1.1\r\n" +
            "Content-Type: text/plain\r\n\r\n" +
            "hello";

        Request request = Request.HttpParser.fromRawString(requestRaw);

        assertEquals("HTTP/1.1", request.protocol);
        assertEquals("POST", request.requestType);
        assertEquals("/submit", request.path);
        assertEquals("text/plain", request.contentType);
        assertEquals(0, request.contentLength); // default/fallback to 0

        assertNull(request.cookie);
        assertNull(request.userAgent);
        
        assertEquals("hello", request.body); // body still present

        assertTrue(request.isOk);
    }

    @Test
    void httpParserTestInvalidRequestFormat() {
        String requestRaw = "INVALID_REQUEST";

        Request request = Request.HttpParser.fromRawString(requestRaw);

        assertFalse(request.isOk);

        assertNull(request.protocol);
        assertNull(request.requestType);
        assertNull(request.path);
        assertNull(request.contentType);
        assertNull(request.cookie);
        assertNull(request.userAgent);
        assertNull(request.body);

        assertEquals(0, request.contentLength);
    }
}
