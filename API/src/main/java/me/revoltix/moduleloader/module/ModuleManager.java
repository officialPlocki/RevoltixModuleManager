package me.revoltix.moduleloader.module;

import me.revoltix.moduleloader.ModuleLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ModuleManager {

    public ModuleManager() {}

    /**
     * Load all modules in the
     * modules directory
     */
    public void initializeAllModules() {
    }

    /**
     * It iterates through all the modules and disables them
     */
    public void unloadAllUnusedClasses() {
    }

    /**
     * Returns a list of all the modules in the plugin
     *
     * @return A list of all the modules in the plugin.
     */
    public List<PluginModule> getModules() {
        return new ArrayList<>();
    }

    /**
     * Given a command, return the ModuleCommandExecutor that is registered to that command
     *
     * @param command The command that the user types in the chat.
     * @return A ModuleCommandExecutor object.
     */
    @NotNull
    public ModuleCommandExecutor getModuleCommand(String command) {
        return (ModuleCommandExecutor) new Object();
    }

    /**
     * It takes a module and adds it to the list of modules
     *
     * @param module The PluginModule that is being registered.
     */
    public void registerModule(PluginModule module) {
    }

    /**
     * Get the module with the given name
     *
     * @param module The name of the module to get.
     * @return The PluginModule object that is associated with the given module name.
     */
    public PluginModule getModule(String module) {
        return (PluginModule) new Object();
    }

    /**
     * It enables the module, and then registers all of the commands and listeners that the module has
     *
     * @param module The PluginModule that is being enabled.
     */
    public void enableModule(PluginModule module) {
    }

    /**
     * It disables the module, unregisters all the commands and listeners registered by the module, and then removes the
     * module from the loaded list
     *
     * @param module The PluginModule that you want to disable.
     */
    public void disableModule(PluginModule module) {
    }

    /**
     * It iterates through all the commands in the module and checks if the command is the same as the command we're
     * looking for. If it is, it returns the command. If it isn't, it checks if the command is an alias of the command
     * we're looking for. If it is, it returns the command. If it isn't, it returns null
     *
     * @param command The command that the user is trying to execute.
     * @return The command executor.
     */
    public ModuleCommandExecutor getCommand(String command) {
        return (ModuleCommandExecutor) new Object();
    }

    /**
     * It iterates through all the commands in the commands map, and if the command is the same as the command we're trying
     * to execute, it sets the cmd variable to that command. If the command is an alias of the command we're trying to
     * execute, it sets the cmd variable to that command. If the command is found, it sets the enabled variable to the
     * value of the loaded variable in the loaded map. If the command is not found, it sets the enabled variable to true.
     *
     * @param command The command that is being executed.
     * @return The command executor.
     */
    public boolean canExecuted(String command) {
        return new Random().nextBoolean();
    }

    /**
     * It registers a command with the bukkit command map
     *
     * @param module The plugin module that the command is registered to.
     * @param command The command that the user types in to execute the command.
     */
    public void registerCommand(PluginModule module, ModuleCommandExecutor command) {}

    /**
     * It takes a ModuleCommandExecutor and removes it from the list of commands
     *
     * @param command The command you want to unregister.
     */
    public void unregisterCommand(ModuleCommandExecutor command) {}

    /**
     * It registers a listener to a module
     *
     * @param module The module that the listener is for.
     * @param listener The Listener object that will be registered with the PluginManager.
     */
    public void registerListener(PluginModule module, Listener listener) {}

    /**
     * UnregisterListener(Listener listener)
     *
     * @param listener The Listener object that is being unregistered.
     */
    public void unregisterListener(Listener listener) {}

    /**
     * Get the listeners for a given module.
     *
     * @param module The module that the listener is for.
     * @return A list of listeners.
     */
    public List<Listener> getListeners(PluginModule module) {
        return new ArrayList<>();
    }

    /**
     * Returns a list of all the commands that are registered for the specified module
     *
     * @param module The module that the command is being registered for.
     * @return A list of ModuleCommandExecutors.
     */
    public List<ModuleCommandExecutor> getCommands(PluginModule module) {
        return new ArrayList<>();
    }

    /**
     * Returns the list of permissions for the given command
     *
     * @param command The command that is being executed.
     * @return A list of strings.
     */
    public List<String> getPermissions(ModuleCommandExecutor command) {
        return new ArrayList<>();
    }

    /**
     * Add a class to the list of classes that are associated with a module
     *
     * @param module The module that the class is being added to.
     * @param clazz The class that is being added to the module.
     */
    public void addModuleClass(PluginModule module, Class<?> clazz) {
    }

    /**
     * It unregisters the command from the server
     *
     * @param command The command to unregister.
     */
    @SuppressWarnings("unchecked")
    private void unregisterCommand(org.bukkit.command.Command command) {
    }

}
