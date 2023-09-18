package me.revoltix.moduleloader.commands;

import me.revoltix.moduleloader.ModuleLoader;
import me.revoltix.moduleloader.module.Module;
import me.revoltix.moduleloader.module.ModuleCommandExecutor;
import me.revoltix.moduleloader.module.PluginModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ModuleCommand implements CommandExecutor {

    /**
     * This function is called when a player uses the /modules command
     *
     * @param sender The player who executed the command.
     * @param command The command that will be executed.
     * @param label The name of the command.
     * @param args The arguments that were passed to the command.
     * @return Nothing.
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.hasPermission("modules.command")) {
            if(args.length == 0) {
                sender.sendMessage(ModuleLoader.prefix + "Bitte benutze /modules <" + ChatColor.YELLOW + "list" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "info" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "disable" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "enable" + ChatColor.GRAY + "> <" + ChatColor.YELLOW + "Module" + ChatColor.GRAY + ">");
            } else if(args.length == 1) {
                if(args[0].equalsIgnoreCase("list")) {
                    sender.sendMessage(ChatColor.BLACK + "== " + ChatColor.AQUA + "" + ChatColor.BOLD + "Modules " + ChatColor.BLACK + "==");
                    sender.sendMessage("");
                    ModuleLoader.getModuleManager().getModules().forEach(module -> sender.sendMessage(ChatColor.GRAY + "== " + ChatColor.AQUA + "" + module.getClass().getAnnotation(Module.class).moduleName()));
                    sender.sendMessage("");
                } else {
                    sender.sendMessage(ModuleLoader.prefix + "Bitte benutze /modules <" + ChatColor.YELLOW + "list" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "info" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "disable" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "enable" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "command" + ChatColor.GRAY + "> <" + ChatColor.YELLOW + "Modul" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "Command" + ChatColor.GRAY + ">");
                }
            } else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("info")) {
                    String mName = args[1];
                    PluginModule module = ModuleLoader.getModuleManager().getModule(mName);
                    if(module != null) {
                        sender.sendMessage(ChatColor.BLACK + "== " + ChatColor.AQUA + "" + ChatColor.BOLD + "Modules " + ChatColor.BLACK + "==");
                        sender.sendMessage("");
                        sender.sendMessage(ChatColor.GRAY + "Modul Name: " + ChatColor.AQUA + "" + module.getClass().getAnnotation(Module.class).moduleName());
                        sender.sendMessage(ChatColor.GRAY + "Befehle:");
                        for (ModuleCommandExecutor moduleCommand : ModuleLoader.getModuleManager().getCommands(module)) {
                            sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.YELLOW + moduleCommand.getClass().getAnnotation(
                                me.revoltix.moduleloader.module.ModuleCommand.class).command().toLowerCase());
                        }
                        sender.sendMessage(ChatColor.GRAY + "Berechtigungen:");
                        for (ModuleCommandExecutor moduleCommand : ModuleLoader.getModuleManager().getCommands(module)) {
                            for(String permission : moduleCommand.getClass().getAnnotation(me.revoltix.moduleloader.module.ModuleCommand.class).permissions()) {
                                sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.YELLOW + permission);
                            }
                        }
                        sender.sendMessage(ChatColor.GRAY + "Listener: " + ChatColor.YELLOW + ModuleLoader.getModuleManager().getListeners(module).size());
                        sender.sendMessage("");
                    } else {
                        sender.sendMessage(ModuleLoader.prefix + "Das Modul wurde nicht gefunden.");
                    }
                } else if(args[0].equalsIgnoreCase("disable")) {
                    PluginModule module = ModuleLoader.getModuleManager().getModule(args[1]);
                    if(module != null) {
                        ModuleLoader.getModuleManager().disableModule(module);
                        sender.sendMessage(ModuleLoader.prefix + "Das Modul wurde deaktiviert.");
                    } else {
                        sender.sendMessage(ModuleLoader.prefix + "Das Modul wurde nicht gefunden.");
                    }
                } else if(args[0].equalsIgnoreCase("enable")) {
                    PluginModule module = ModuleLoader.getModuleManager().getModule(args[1]);
                    if(module != null) {
                        ModuleLoader.getModuleManager().enableModule(module);
                        sender.sendMessage(ModuleLoader.prefix + "Das Modul wurde aktiviert.");
                    } else {
                        sender.sendMessage(ModuleLoader.prefix + "Das Modul wurde nicht gefunden.");
                    }
                } else if(args[0].equalsIgnoreCase("command")) {
                    ModuleCommandExecutor cmd = ModuleLoader.getModuleManager().getModuleCommand(args[1]);
                    if(cmd != null) {
                        me.revoltix.moduleloader.module.ModuleCommand
                            annotation = cmd.getClass().getAnnotation(me.revoltix.moduleloader.module.ModuleCommand.class);
                        sender.sendMessage(ChatColor.BLACK + "== " + ChatColor.AQUA + "" + ChatColor.BOLD + "Modules " + ChatColor.BLACK + "==");
                        sender.sendMessage("");
                        sender.sendMessage(ChatColor.GRAY + "TabComplete: " + ChatColor.YELLOW + annotation.tabCompleterIsEnabled());
                        sender.sendMessage(ChatColor.GRAY + "Befehl: " + ChatColor.YELLOW + annotation.command());
                        sender.sendMessage(ChatColor.GRAY + "Usage: " + ChatColor.YELLOW + annotation.usage());
                        sender.sendMessage(ChatColor.GRAY + "Beschreibung: " + ChatColor.YELLOW + annotation.description());
                        sender.sendMessage(ChatColor.GRAY + "Aliase:");
                        for (String alias : annotation.aliases()) {
                            sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.YELLOW + alias);
                        }
                        sender.sendMessage(ChatColor.GRAY + "Berechtigungen:");
                        for(String permission : ModuleLoader.getModuleManager().getPermissions(cmd)) {
                            sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.YELLOW + permission);
                        }
                        sender.sendMessage("");
                    } else {
                        sender.sendMessage(ModuleLoader.prefix + "Der Befehl konnte nicht gefunden werden.");
                    }
                } else {
                    sender.sendMessage(ModuleLoader.prefix + "Bitte benutze /modules <" + ChatColor.YELLOW + "list" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "info" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "disable" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "enable" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "command" + ChatColor.GRAY + "> <" + ChatColor.YELLOW + "Modul" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "Command" + ChatColor.GRAY + ">");
                }
            } else {
                sender.sendMessage(ModuleLoader.prefix + "Bitte benutze /modules <" + ChatColor.YELLOW + "list" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "info" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "disable" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "enable" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "command" + ChatColor.GRAY + "> <" + ChatColor.YELLOW + "Modul" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "Command" + ChatColor.GRAY + ">");
            }
        } else {
            sender.sendMessage(ModuleLoader.prefix + "Du hast dazu keine Rechte.");
        }
        return false;
    }

}
