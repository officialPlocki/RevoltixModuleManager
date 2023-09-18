package me.revoltix.moduleloader.listeners;

import me.revoltix.moduleloader.ModuleLoader;
import me.revoltix.moduleloader.module.ModuleCommand;
import me.revoltix.moduleloader.module.ModuleCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;

public class CommandListener implements Listener {

    /**
     * If the command is a module command, and the tab completer is enabled, then set the completions to the tab completer
     *
     * @param event The event that is being tab completed.
     */
    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        String[] commandArgs = event.getBuffer().replaceFirst("/", "").replaceAll("-fallback", "").split(" ");
        ModuleCommandExecutor cmd = ModuleLoader.getModuleManager().getCommand(commandArgs[0]);
        if(cmd != null) {
            if(cmd.getClass().getAnnotation(ModuleCommand.class).tabCompleterIsEnabled()) {
                event.setCompletions(cmd.getTabCompletions(commandArgs));
            } else {
                event.setCompletions(new ArrayList<>());
            }
        } else {
            event.setCompletions(new ArrayList<>());
        }
    }

    /**
     * If the command is not in the list of commands, it will be cancelled
     *
     * @param event The event that is being processed.
     */
    @EventHandler
    public void onProcess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().replaceFirst("/", "").replaceAll("-fallback", "").split(" ")[0];
        if(!ModuleLoader.getModuleManager().canExecuted(command)) {
            event.setCancelled(true);
        }
        if(command.contains("bukkit:") ||
                command.equalsIgnoreCase("?") ||
                command.equalsIgnoreCase("about") ||
                command.equalsIgnoreCase("help") ||
                command.equalsIgnoreCase("pl") ||
                command.equalsIgnoreCase("plugins") ||
                command.equalsIgnoreCase("reload") ||
                command.equalsIgnoreCase("rl") ||
                command.equalsIgnoreCase("ver") ||
                command.equalsIgnoreCase("version") ||
                command.equalsIgnoreCase("minecraft:me") ||
                command.equalsIgnoreCase("me") ||
                command.equalsIgnoreCase("minecraft:help") ||
                command.equalsIgnoreCase("minecraft:kick") ||
                command.equalsIgnoreCase("minecraft:msg") ||
                command.equalsIgnoreCase("minecraft:ban") ||
                command.equalsIgnoreCase("minecraft:ban-ip") ||
                command.equalsIgnoreCase("minecraft:pardon") ||
                command.equalsIgnoreCase("minecraft:pardon-ip") ||
                command.equalsIgnoreCase("pardon-ip") ||
                command.equalsIgnoreCase("recipe") ||
                command.equalsIgnoreCase("ban-ip") ||
                command.equalsIgnoreCase("say") ||
                command.equalsIgnoreCase("minecraft:say") ||
                command.equalsIgnoreCase("tell") ||
                command.equalsIgnoreCase("minecraft:tell") ||
                command.equalsIgnoreCase("tellraw") ||
                command.equalsIgnoreCase("minecraft:tellraw")) {
            if(!event.getPlayer().hasPermission("modules.bypass")) {
                event.setCancelled(true);
            }
        }
        if(!Bukkit.getServer().getCommandMap().getKnownCommands().containsKey(command)) {
            event.setCancelled(true);
        }
        if(event.isCancelled()) {
            event.getPlayer().sendMessage(ModuleLoader.prefix + ModuleLoader.cmdNotFound);
        }
    }

}
