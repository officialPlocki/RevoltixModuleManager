package xyz.plocki.licensing.util;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class UpdateSocket implements HttpHandler {

    private final String version = "a1.0.1-RELEASE";
    private final String changelog = "Added update notifications%n" +
            "Added new exceptions for better module handling%n" +
            "Updated license system%n" +
            "Migrated to Java 16";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = Base64.getEncoder().encodeToString((version + "§%" + changelog).getBytes(StandardCharsets.UTF_8));
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().flush();
        exchange.getResponseBody().close();
    }

}
