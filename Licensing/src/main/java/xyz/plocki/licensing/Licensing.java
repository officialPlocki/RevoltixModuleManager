package xyz.plocki.licensing;

import com.sun.net.httpserver.HttpServer;
import org.apache.commons.lang3.RandomStringUtils;
import xyz.plocki.licensing.license.LicenseManager;
import xyz.plocki.licensing.util.Adminsocket;
import xyz.plocki.licensing.util.UpdateSocket;
import xyz.plocki.licensing.util.Websocket;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.Random;

public class Licensing {

    public static String adminKey = "";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(81), 0);
        server.createContext("/admin", new Adminsocket());
        server.createContext("/activation", new Websocket());
        server.createContext("/currentVersion", new UpdateSocket());
        server.setExecutor(null);
        server.start();
        long time = Long.parseLong("2419200000");
        try {
            File file = new File("latest.log");
            if(file.exists()) {
                file.renameTo(new File("latest.log." + new Random().nextInt(Integer.MAX_VALUE)));
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(() -> {
            for(;;) {
                adminKey = RandomStringUtils.randomAlphabetic(12);
                System.out.println("ADMIN » New Admin Key -> " + adminKey);
                try {
                    Thread.sleep(1000*(5*60));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Thread timer = new Thread(() -> {
            for(;;) {
                LicenseManager lm = new LicenseManager();
                lm.getLicenses().forEach(license -> {
                    if((System.currentTimeMillis() - lm.getLastActivation(license)) >= time) {
                        lm.deleteLicense(license);
                        log("License " + license + " delete because of inactivity (longer than 4 weeks).");
                    }
                });
                try {
                    Thread.sleep(86400000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            thread.stop();
            timer.stop();
            server.stop(1);
        }));
    }

    public static void log(String s) {
        System.out.println(s);
        File file = new File("latest.log");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(file, true), true);
            writer.append("\n");
            writer.append(s);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
