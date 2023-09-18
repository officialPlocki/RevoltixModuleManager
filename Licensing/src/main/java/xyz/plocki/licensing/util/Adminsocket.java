package xyz.plocki.licensing.util;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import xyz.plocki.licensing.Licensing;
import xyz.plocki.licensing.license.LicenseManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Adminsocket implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
        AtomicReference<String> adminKey = new AtomicReference<>("");
        AtomicBoolean create = new AtomicBoolean(true);
        AtomicReference<String> license = new AtomicReference<>("");

        params.forEach((field, value) -> {
            if(field.equalsIgnoreCase("adminKey")) {
                adminKey.set(value);
            }
            if(field.equalsIgnoreCase("create")) {
                create.set(Boolean.parseBoolean(value));
            }
            if(field.equalsIgnoreCase("license")) {
                license.set(value);
            }
        });
        if(adminKey.get().equals("")) {
            System.out.println("ADMIN » IP " + exchange.getRemoteAddress().getHostName() + " failed auth!");
            String response = "error";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().flush();
            exchange.getResponseBody().close();
        } else {
            if(adminKey.get().equals(Licensing.adminKey)) {
                LicenseManager m = new LicenseManager();
                if(create.get()) {
                    String response = m.createLicense();
                    Licensing.log("ADMIN » Created license " + response + ", command from IP " + exchange.getRemoteAddress().getHostName());
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.getResponseBody().flush();
                    exchange.getResponseBody().close();
                } else {
                    if(license.get().equals("")) {
                        String response = "error";
                        Licensing.log("ADMIN » Command from IP " + exchange.getRemoteAddress().getHostName() + " failed!");
                        exchange.sendResponseHeaders(200, response.length());
                        exchange.getResponseBody().write(response.getBytes());
                        exchange.getResponseBody().flush();
                        exchange.getResponseBody().close();
                    } else {
                        m.deleteLicense(license.get());
                        Licensing.log("ADMIN » Deleted license " + license.get() + ", command from IP " + exchange.getRemoteAddress().getHostName());
                        String response = "deleted!";
                        exchange.sendResponseHeaders(200, response.length());
                        exchange.getResponseBody().write(response.getBytes());
                        exchange.getResponseBody().flush();
                        exchange.getResponseBody().close();
                    }
                }
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
