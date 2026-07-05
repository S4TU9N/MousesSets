package me.mouseman.mousessets.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProgressService {

    private final JavaPlugin plugin;

    private final File file;
    private final FileConfiguration config;

    // runtime cache
    private final Map<UUID, Map<String, Integer>> cache = new HashMap<>();

    // dirty flag (prevents unnecessary saves)
    private boolean dirty = false;

    public ProgressService(JavaPlugin plugin) {
        this.plugin = plugin;

        this.file = new File(plugin.getDataFolder(), "progress.yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource("progress.yml", false);
        }

        this.config = YamlConfiguration.loadConfiguration(file);

        loadFromYaml();
        startAutoSave();
    }

    private void loadFromYaml() {

        if (!config.contains("players")) return;

        for (String uuidStr : config.getConfigurationSection("players").getKeys(false)) {

            UUID uuid = UUID.fromString(uuidStr);

            Map<String, Integer> playerData = new HashMap<>();

            for (String setId : config.getConfigurationSection("players." + uuidStr).getKeys(false)) {
                playerData.put(
                        setId,
                        config.getInt("players." + uuidStr + "." + setId)
                );
            }

            cache.put(uuid, playerData);
        }
    }

    public int getProgress(UUID uuid, String setId) {

        return cache
                .getOrDefault(uuid, Map.of())
                .getOrDefault(setId, 0);
    }

    public void setProgress(UUID uuid, String setId, int value) {

        cache
                .computeIfAbsent(uuid, k -> new HashMap<>())
                .put(setId, value);

        dirty = true;
    }

    private void startAutoSave() {

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(
                plugin,
                this::saveToYaml,
                20L * 60,   // 1 minute delay before first save
                20L * 60    // repeat every 1 minute
        );
    }

    public void saveToYaml() {

        if (!dirty) return;

        config.set("players", null);

        for (Map.Entry<UUID, Map<String, Integer>> entry : cache.entrySet()) {

            UUID uuid = entry.getKey();

            for (Map.Entry<String, Integer> setEntry : entry.getValue().entrySet()) {

                config.set(
                        "players." + uuid + "." + setEntry.getKey(),
                        setEntry.getValue()
                );
            }
        }

        try {
            config.save(file);
            dirty = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}