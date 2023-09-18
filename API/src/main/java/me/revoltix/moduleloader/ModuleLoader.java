package me.revoltix.moduleloader;

import me.revoltix.moduleloader.module.ModuleManager;
import me.revoltix.moduleloader.util.ModuleScheduler;
import me.revoltix.moduleloader.util.mysql.MySQLService;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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

    /**
     * This function is called when the plugin is enabled
     */
    @Override
    public void onEnable() {
        ml = this;
        plugin = this;
        mm = null;
        service = null;
        scheduler = null;
    }

    @Override
    // This is a function that is called when the plugin is disabled.
    public void onDisable() {}

    /**
     * This function returns the scheduler object
     *
     * @return The scheduler object.
     */
    @NotNull
    public static ModuleScheduler getScheduler() {
        return scheduler;
    }

    /**
     * Returns the module loader
     *
     * @return The module loader.
     */
    @NotNull
    public static ModuleLoader getModuleLoader() {
        return ml;
    }

    /**
     * This function returns the module manager
     *
     * @return The singleton instance of the ModuleManager class.
     */
    @NotNull
    public static ModuleManager getModuleManager() {
        return mm;
    }

    /**
     * This function returns the singleton instance of the MySQLService class
     *
     * @return The service object.
     */
    @NotNull
    public static MySQLService getMySQLService() { return service; }

    /**
     * This function returns the plugin object
     *
     * @return The plugin object.
     */
    @NotNull
    public static Plugin getPlugin() {
        return plugin;
    }

}
