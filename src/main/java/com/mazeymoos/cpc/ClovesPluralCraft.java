package com.mazeymoos.cpc;

import org.bukkit.plugin.java.JavaPlugin;
import com.mazeymoos.cpc.commands.SystemCommand;
import com.mazeymoos.cpc.commands.FrontCommand;
import com.mazeymoos.cpc.listeners.ChatProxyListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ClovesPluralCraft extends JavaPlugin {
    public static final String MOD_ID = "cloves-pluralcraft";
    public static final String MOD_NAME = "Clove's PluralCraft";
    private static final Path CONFIG_DIR = Path.of("plugins/CPC");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Map<UUID, SystemData> systemDataMap = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("[CPC] " + MOD_NAME + " Loaded!");
        loadAllSystems();

        getCommand("system").setExecutor(new SystemCommand());
        getCommand("front").setExecutor(new FrontCommand());

        getServer().getPluginManager().registerEvents(new ChatProxyListener(), this);
    }

    @Override
    public void onDisable() {
        saveAllSystems();
    }

    public static void loadAllSystems() {
        try {
            Files.createDirectories(CONFIG_DIR);
            Files.list(CONFIG_DIR).forEach(path -> {
                try {
                    UUID uuid = UUID.fromString(path.getFileName().toString().replace(".json", ""));
                    SystemData data = GSON.fromJson(Files.readString(path), SystemData.class);
                    systemDataMap.put(uuid, data);
                } catch (Exception e) {
                    System.err.println("Failed to load system file: " + path);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveAllSystems() {
        systemDataMap.forEach((uuid, system) -> saveSystem(uuid));
    }

    public static void saveSystem(UUID uuid) {
        try {
            Path filePath = CONFIG_DIR.resolve(uuid.toString() + ".json");
            Files.writeString(filePath, GSON.toJson(systemDataMap.get(uuid)));
        } catch (IOException e) {
            System.err.println("Failed to save system file: " + uuid);
        }
    }

    public static class SystemData {
        public String systemName;
        public Map<String, Boolean> fronts = new HashMap<>();
        public String activeFront = "";

        public SystemData(String systemName) {
            this.systemName = systemName;
        }
    }
}
