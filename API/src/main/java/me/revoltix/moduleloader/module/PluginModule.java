package me.revoltix.moduleloader.module;

import me.revoltix.moduleloader.ModuleLoader;
import me.revoltix.moduleloader.util.ModuleScheduler;
import me.revoltix.moduleloader.util.mysql.MySQLService;
import org.bukkit.plugin.Plugin;

public abstract class PluginModule {

    /**
     * This function is called when the module is enabled.
     */
    public void enableModule() {
    }

    /**
     * This function is called when the module is disabled.
     */
    public void disableModule() {
    }

    /**
     * This function returns the MySQLService object
     *
     * @return The MySQLService object.
     */
    public MySQLService getMySQLService() {
        return ModuleLoader.getMySQLService();
    }

    /**
     * Returns the module loader
     *
     * @return The singleton instance of the ModuleLoader class.
     */
    public ModuleLoader getModuleLoader() {
        return ModuleLoader.getModuleLoader();
    }

    /**
     * This function returns the module manager
     *
     * @return The ModuleManager instance.
     */
    public ModuleManager getModuleManager() {
        return ModuleLoader.getModuleManager();
    }

    /**
     * This function returns the plugin that is currently running
     *
     * @return The plugin object.
     */
    public Plugin getPlugin() {
        return ModuleLoader.getPlugin();
    }

    /**
     * Returns the scheduler for the current module
     *
     * @return The scheduler.
     */
    public ModuleScheduler getScheduler() {
        return ModuleLoader.getScheduler();
    }

}