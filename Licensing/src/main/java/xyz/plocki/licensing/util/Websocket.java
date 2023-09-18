package xyz.plocki.licensing.util;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.lang3.RandomStringUtils;
import xyz.plocki.licensing.Licensing;
import xyz.plocki.licensing.license.LicenseManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Websocket implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
        AtomicReference<String> license = new AtomicReference<>("");
        params.forEach((field, value) -> {
            if(field.equalsIgnoreCase("license")) {
                license.set(value);
            }
        });
        if(license.get().equals("")) {
            String response = Base64.getEncoder().encodeToString(("false:" + RandomStringUtils.randomAlphabetic(12)).getBytes(StandardCharsets.UTF_8));
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().flush();
            exchange.getResponseBody().close();
        } else {
            LicenseManager m = new LicenseManager();
            if(m.licenseExist(license.get())) {
                Licensing.log("Activated license " + license.get() + " for IP " + exchange.getRemoteAddress().getHostName());
                m.activateLicense(license.get());
                String response = Base64.getEncoder().encodeToString(("true:" + System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8));
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseHeaders().add("Verification-Key", RandomStringUtils.randomAlphabetic(2000));
                exchange.getResponseBody().flush();
                exchange.getResponseBody().close();
            } else {
                String response = Base64.getEncoder().encodeToString(("false:" + RandomStringUtils.randomAlphabetic(12)).getBytes(StandardCharsets.UTF_8));
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().flush();
                exchange.getResponseBody().close();
            }
        }
    }

    private Map<String, String> queryToMap(String query) {
        if(query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

}
