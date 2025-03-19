package com.mazeymoos.cpc.commands;

import com.mazeymoos.cpc.ClovesPluralCraft;
// import com.mazeymoos.cpc.utils.SkinManager; - Work in Progress
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
            sender.sendMessage(ChatColor.RED + "Usage: /front <add|delete|set|clear|skin> [name] [skin]");
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
                setFront(player, args[1], sender);
                break;
            case "clear":
                clearFront(playerUUID, sender);
                break;
            case "skin":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /front skin <front> <skin (URL or Username)>");
                    return true;
                }
                setFrontSkin(playerUUID, args[1], args[2], sender);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand! Use /front <add|delete|set|clear|skin>");
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
        ClovesPluralCraft.systemDataMap.get(uuid).frontSkins.remove(frontName); // Remove associated skin
        ClovesPluralCraft.saveSystem(uuid);
        sender.sendMessage(ChatColor.GREEN + "Front '" + frontName + "' deleted!");
    }

    private void setFront(Player player, String frontName, CommandSender sender) {
        UUID uuid = player.getUniqueId();

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

        // Apply skin when fronting
        String skin = ClovesPluralCraft.systemDataMap.get(uuid).frontSkins.get(frontName);
        if (skin != null && !skin.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "This has not been implemented yet!");
            // SkinManager.applySkin(player, skin);
        }

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

    private void setFrontSkin(UUID uuid, String frontName, String skinInput, CommandSender sender) {
        if (!ClovesPluralCraft.systemDataMap.containsKey(uuid)) {
            sender.sendMessage(ChatColor.RED + "You do not have a system!");
            return;
        }
        if (!ClovesPluralCraft.systemDataMap.get(uuid).fronts.containsKey(frontName)) {
            sender.sendMessage(ChatColor.RED + "Front '" + frontName + "' does not exist!");
            return;
        }

        ClovesPluralCraft.systemDataMap.get(uuid).frontSkins.put(frontName, skinInput);
        ClovesPluralCraft.saveSystem(uuid);
        sender.sendMessage(ChatColor.GREEN + "Skin set for front '" + frontName + "'!");

        // Test the skin immediately if this is the active front
        if (frontName.equals(ClovesPluralCraft.systemDataMap.get(uuid).activeFront)) {
            Player player = (Player) sender;
            player.sendMessage(ChatColor.YELLOW + "Applying skin...");
            // SkinManager.applySkin(player, skinInput);
        }
    }
}