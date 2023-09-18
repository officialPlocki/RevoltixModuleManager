package me.revoltix.moduleloader;

import me.revoltix.moduleloader.commands.ModuleCommand;
import me.revoltix.moduleloader.listeners.CommandListener;
import me.revoltix.moduleloader.module.ModuleManager;
import me.revoltix.moduleloader.util.ModuleScheduler;
import me.revoltix.moduleloader.util.Updater;
import me.revoltix.moduleloader.util.files.FileBuilder;
import me.revoltix.moduleloader.util.mysql.MySQLService;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Objects;

public final class ModuleLoader extends JavaPlugin {

    // This is a static variable that holds the plugin instance.
    private static Plugin plugin;
    // This is a static variable that holds the module manager.
    private static ModuleManager mm;
    // This is a static variable that holds the plugin instance.
    private static ModuleLoader ml;
    // This is a static variable that holds the MySQL service.
    private static MySQLService service;
    private static ModuleScheduler scheduler;
    public static String prefix = "";
    public static String cmdNotFound = "";
    public static boolean postOnHastebin = false;

    /**
     * This function is called when the plugin is enabled
     */
    @Override
    public void onEnable() {
        try {
            if(!new Updater().checkUpdate()) {
                Bukkit.getPluginManager().disablePlugin(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.getConsoleSender().sendMessage("\n\n\nHastebin post by Kaimu\nModulSystem by plocki.\nhttps://revoltix.me\n\n\n");
        ml = this;
        plugin = this;
        scheduler = new ModuleScheduler();
        mm = new ModuleManager();
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Objects.requireNonNull(getCommand("modules")).setExecutor(new ModuleCommand());
        Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
        FileBuilder fb = new FileBuilder("plugins/ModuleManager/config.yml");
        YamlConfiguration yml = fb.getYaml();
        if(!yml.isSet("mysql.host")) {
            yml.set("mysql.host", "0.0.0.0");
            yml.set("mysql.port", 3306);
            yml.set("mysql.database", "database");
            yml.set("mysql.user", "user");
            yml.set("mysql.password", "password");
            yml.set("modules.messages.prefix", "PREFIX");
            yml.set("modules.messages.commandNotFound", "Der Befehl wurde nicht gefunden!");
            yml.set("modules.errors.postOnHastebin", false);

            yml.set("modules.errors", "#Hastebin Website: https://www.toptal.com/developers/hastebin | DSGVO (Englisch): https://www.toptal.com/privacy");
            yml.set("modules.requestLicenseKey", "#Du kannst eine Lizenz im Bereich \"Support\" Anfragen. https://revoltix.me");

            yml.set("modules.licenseKey", "yourLicenseKey");
            fb.save();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        postOnHastebin = yml.getBoolean("modules.errors.postOnHastebin");
        prefix = yml.getString("modules.messages.prefix");
        cmdNotFound = yml.getString("modules.messages.commandNotFound");
        Bukkit.getConsoleSender().sendMessage("Trying to validate License...");
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new URL("http://licensing.revoltix.me:81/activation?license=" + yml.getString("modules.licenseKey")).openStream());
            byte[] contents = new byte[1024];
            int bytesRead;
            String response = null;
            while ((bytesRead = inputStream.read(contents)) != -1) {
                response = new String(contents, 0, bytesRead);
            }
            response = new String(Base64.getDecoder().decode(response.getBytes(StandardCharsets.UTF_8)));
            if(Boolean.parseBoolean(response.split(":")[0])) {
                if((System.currentTimeMillis() - Long.parseLong(response.split(":")[1])) < 10000) {
                    Bukkit.getConsoleSender().sendMessage("License validated!");
                } else {
                    Bukkit.getConsoleSender().sendMessage("License is invalid!");
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage("License is invalid!");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("Cannot connect to license server!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        service = new MySQLService();
        try {
            service.connect(
                yml.getString("mysql.host"),
                yml.getInt("mysql.port"),
                yml.getString("mysql.database"),
                yml.getString("mysql.user"),
                yml.getString("mysql.password")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mm.initializeAllModules();
    }

    @Override
    // This is a function that is called when the plugin is disabled.
    public void onDisable() {
        mm.unloadAllUnusedClasses();
        service.disconnect();
    }

    /**
     * This function returns the scheduler object
     *
     * @return The scheduler object.
     */
    public static ModuleScheduler getScheduler() {
        return scheduler;
    }

    /**
     * Returns the module loader
     *
     * @return The module loader.
     */
    public static ModuleLoader getModuleLoader() {
        return ml;
    }

    /**
     * This function returns the module manager
     *
     * @return The singleton instance of the ModuleManager class.
     */
    public static ModuleManager getModuleManager() {
        return mm;
    }

    /**
     * This function returns the singleton instance of the MySQLService class
     *
     * @return The service object.
     */
    public static MySQLService getMySQLService() { return service; }

    /**
     * This function returns the plugin object
     *
     * @return The plugin object.
     */
    public static Plugin getPlugin() {
        return plugin;
    }



}
