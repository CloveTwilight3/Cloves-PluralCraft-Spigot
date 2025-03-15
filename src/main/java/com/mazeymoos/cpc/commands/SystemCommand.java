package com.mazeymoos.cpc.commands;

import com.mazeymoos.cpc.ClovesPluralCraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.util.UUID;

public class SystemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        UUID playerUUID = player.getUniqueId();

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /system <create|rename|remove> [name]");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /system create <name>");
                    return true;
                }
                createSystem(playerUUID, args[1], sender);
                break;

            case "rename":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /system rename <newName>");
                    return true;
                }
                renameSystem(playerUUID, args[1], sender);
                break;

            case "remove":
                removeSystem(playerUUID, sender);
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand. Use /system <create|rename|remove> [name]");
                break;
        }
        return true;
    }

    private void createSystem(UUID uuid, String name, CommandSender sender) {
        if (ClovesPluralCraft.systemDataMap.containsKey(uuid)) {
            sender.sendMessage(ChatColor.RED + "You already have a system!");
            return;
        }

        ClovesPluralCraft.SystemData newSystem = new ClovesPluralCraft.SystemData(name);
        ClovesPluralCraft.systemDataMap.put(uuid, newSystem);
        ClovesPluralCraft.saveSystem(uuid);
        sender.sendMessage(ChatColor.GREEN + "System '" + name + "' created!");
    }

    private void renameSystem(UUID uuid, String newName, CommandSender sender) {
        if (!ClovesPluralCraft.systemDataMap.containsKey(uuid)) {
            sender.sendMessage(ChatColor.RED + "You do not have a system!");
            return;
        }

        ClovesPluralCraft.systemDataMap.get(uuid).systemName = newName;
        ClovesPluralCraft.saveSystem(uuid);
        sender.sendMessage(ChatColor.GREEN + "System renamed to '" + newName + "'!");
    }

    private void removeSystem(UUID uuid, CommandSender sender) {
        if (!ClovesPluralCraft.systemDataMap.containsKey(uuid)) {
            sender.sendMessage(ChatColor.RED + "You do not have a system to remove!");
            return;
        }

        ClovesPluralCraft.systemDataMap.remove(uuid);
        ClovesPluralCraft.saveSystem(uuid);
        sender.sendMessage(ChatColor.GREEN + "Your system has been removed!");
    }
}
