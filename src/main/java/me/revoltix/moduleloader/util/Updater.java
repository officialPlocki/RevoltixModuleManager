package me.revoltix.moduleloader.util;

import org.bukkit.Bukkit;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

public class Updater {

    public boolean checkUpdate() throws IOException {
        BufferedInputStream inputStream = new BufferedInputStream(new URL("http://licensing.revoltix.me:81/currentVersion").openStream());
        byte[] contents = new byte[1024];
        int bytesRead;
        String response = null;
        while ((bytesRead = inputStream.read(contents)) != -1) {
            response = new String(contents, 0, bytesRead);
        }
        response = new String(Base64.getDecoder().decode(response.getBytes(StandardCharsets.UTF_8)));
        if(response.split("§%")[0].equalsIgnoreCase(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("ModuleManager")).getDescription().getVersion())) {
            Bukkit.getConsoleSender().sendMessage("ModuleManager is on the newest version.");
            return true;
        }
        Bukkit.getConsoleSender().sendMessage("\n\n\n\n\nAn update is ready for ModuleManager, please download it at http://revoltix.me.\nThe support for this version (" + Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("ModuleManager")).getDescription().getVersion() + ") has ended.\nPlease update your Modules to the version " + response.split("§%")[0] + ".\nChangelogs:\n" + response.split("§%")[1].replaceAll("%n", "\n") + "\n\n\n\n\n");
        return false;
    }

}
