package com.mazeymoos.cpc.commands;

import com.mazeymoos.cpc.ClovesPluralCraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FrontCommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null; // No suggestions for console users
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        if (args.length == 1) {
            return List.of("add", "delete", "set", "clear", "skin"); // Main subcommands
        }

        if (!ClovesPluralCraft.systemDataMap.containsKey(playerUUID)) {
            return null; // No suggestions if the player doesn't have a system
        }

        Map<String, Boolean> fronts = ClovesPluralCraft.systemDataMap.get(playerUUID).fronts;

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                return List.of("<name>"); // Placeholder for adding a new front
            } else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("skin")) {
                return new ArrayList<>(fronts.keySet()); // Suggest existing fronts
            }
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("skin")) {
            return List.of("<url or username>"); // Placeholder for skin input
        }

        if (args[0].equalsIgnoreCase("clear")) {
            return List.of(); // No suggestions for /front clear
        }

        return null;
    }
}
