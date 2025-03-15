package com.mazeymoos.cpc.listeners;

import com.mazeymoos.cpc.ClovesPluralCraft;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChatProxyListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (!ClovesPluralCraft.systemDataMap.containsKey(playerUUID)) {
            return; // Ignore players without a system
        }

        ClovesPluralCraft.SystemData systemData = ClovesPluralCraft.systemDataMap.get(playerUUID);
        if (systemData.activeFront == null || systemData.activeFront.isEmpty()) {
            return; // Ignore players who aren't fronting
        }

        String formattedMessage = ChatColor.GRAY + "<" + ChatColor.WHITE + systemData.activeFront + ChatColor.GRAY + " (" + ChatColor.AQUA + systemData.systemName + ChatColor.GRAY + ")> " + ChatColor.RESET + event.getMessage();
        event.setFormat(formattedMessage);
    }
}
