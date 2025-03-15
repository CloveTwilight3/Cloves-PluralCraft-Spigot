package com.mazeymoos.cpc.commands;

import com.mazeymoos.cpc.ClovesPluralCraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FrontCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /front <add|delete|set|clear> [name]");
            return true;
        }

        String action = args[0].toLowerCase();
        switch (action) {
            case "add":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /front add <name>");
                    return true;
                }
                addFront(playerUUID, args[1], sender);
                break;
            case "delete":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /front delete <name>");
                    return true;
                }
                deleteFront(playerUUID, args[1], sender);
                break;
            case "set":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /front set <name>");
                    return true;
                }
                setFront(playerUUID, args[1], sender);
                break;
            case "clear":
                clearFront(playerUUID, sender);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand! Use /front <add|delete|set|clear>");
        }
        return true;
    }

    private void addFront(UUID uuid, String frontName, CommandSender sender) {
        if (!ClovesPluralCraft.systemDataMap.containsKey(uuid)) {
            sender.sendMessage(ChatColor.RED + "You do not have a system!");
            return;
        }
        ClovesPluralCraft.systemDataMap.get(uuid).fronts.put(frontName, true);
        ClovesPluralCraft.saveSystem(uuid);
        sender.sendMessage(ChatColor.GREEN + "Front '" + frontName + "' added!");
    }

    private void deleteFront(UUID uuid, String frontName, CommandSender sender) {
        if (!ClovesPluralCraft.systemDataMap.containsKey(uuid)) {
            sender.sendMessage(ChatColor.RED + "You do not have a system!");
            return;
        }
        if (!ClovesPluralCraft.systemDataMap.get(uuid).fronts.containsKey(frontName)) {
            sender.sendMessage(ChatColor.RED + "Front '" + frontName + "' does not exist!");
            return;
        }
        ClovesPluralCraft.systemDataMap.get(uuid).fronts.remove(frontName);
        ClovesPluralCraft.saveSystem(uuid);
        sender.sendMessage(ChatColor.GREEN + "Front '" + frontName + "' deleted!");
    }

    private void setFront(UUID uuid, String frontName, CommandSender sender) {
        if (!ClovesPluralCraft.systemDataMap.containsKey(uuid)) {
            sender.sendMessage(ChatColor.RED + "You do not have a system!");
            return;
        }
        if (!ClovesPluralCraft.systemDataMap.get(uuid).fronts.containsKey(frontName)) {
            sender.sendMessage(ChatColor.RED + "Front '" + frontName + "' does not exist!");
            return;
        }
        ClovesPluralCraft.systemDataMap.get(uuid).activeFront = frontName;
        ClovesPluralCraft.saveSystem(uuid);
        sender.sendMessage(ChatColor.GREEN + "Now fronting as '" + frontName + "'!");
    }

    private void clearFront(UUID uuid, CommandSender sender) {
        if (!ClovesPluralCraft.systemDataMap.containsKey(uuid)) {
            sender.sendMessage(ChatColor.RED + "You do not have a system!");
            return;
        }
        ClovesPluralCraft.systemDataMap.get(uuid).activeFront = "";
        ClovesPluralCraft.saveSystem(uuid);
        sender.sendMessage(ChatColor.GREEN + "Front cleared!");
    }
}
