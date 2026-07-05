package me.mouseman.mousessets.managers;

import me.mouseman.mousessets.sets.MilestoneSet;
import me.mouseman.mousessets.definitions.TriggerType;
import org.bukkit.entity.EntityType;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SetLoader {

    private final JavaPlugin plugin;

    public SetLoader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private TriggerType safeTriggerType(String value, String path, String setId) {
        if (value == null) return null;

        try {
            return TriggerType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("[MousesSets] Invalid TriggerType '" + value + "' in " + setId + " at " + path);
            return null;
        }
    }

    private Material safeMaterial(String value, String path, String setId) {
        if (value == null) return null;

        try {
            return Material.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("[MousesSets] Invalid Material '" + value + "' in " + setId + " at " + path);
            return null;
        }
    }

    private Statistic safeStatistic(String value, String path, String setId) {
        if (value == null) return null;

        try {
            return Statistic.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("[MousesSets] Invalid Statistic '" + value + "' in " + setId + " at " + path);
            return null;
        }
    }

    private EntityType safeEntity(String value, String path, String setId) {
        if (value == null) return null;

        try {
            return EntityType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("[MousesSets] Invalid EntityType '" + value + "' in " + setId + " at " + path);
            return null;
        }
    }

    public Map<String, MilestoneSet> loadSets() {

        File file = new File(plugin.getDataFolder(), "sets.yml");

        if (!file.exists()) {
            plugin.saveResource("sets.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        Map<String, MilestoneSet> sets = new HashMap<>();

        for (String id : config.getKeys(false)) {

            String base = id + ".";

            TriggerType type = safeTriggerType(
                    config.getString(base + "trigger.type"),
                    base + "trigger.type",
                    id
            );

            Statistic statistic = safeStatistic(
                    config.getString(base + "trigger.statistic"),
                    base + "trigger.statistic",
                    id
            );

            if (type == null || statistic == null) {
                plugin.getLogger().warning("[MousesSets] Skipping invalid set: " + id);
                continue;
            }

            Material materialTarget = safeMaterial(
                    config.getString(base + "trigger.material"),
                    base + "trigger.material",
                    id
            );

            EntityType entityTarget = safeEntity(
                    config.getString(base + "trigger.entity"),
                    base + "trigger.entity",
                    id
            );

            if ((type == TriggerType.BLOCK_MINED
                    || type == TriggerType.ITEM_USED
                    || type == TriggerType.ITEM_CRAFTED
                    || type == TriggerType.ITEM_BROKEN)
                    && materialTarget == null) {

                plugin.getLogger().warning("[MousesSets] Missing valid material for set: " + id);
                continue;
            }

            if ((type == TriggerType.ENTITY_KILLED
                    || type == TriggerType.ENTITY_KILLED_BY)
                    && entityTarget == null) {

                plugin.getLogger().warning("[MousesSets] Missing valid entity for set: " + id);
                continue;
            }

            int helmet = config.getInt(base + "requirements.helmet");
            int chestplate = config.getInt(base + "requirements.chestplate");
            int leggings = config.getInt(base + "requirements.leggings");
            int boots = config.getInt(base + "requirements.boots"); 

            sets.put(id, new MilestoneSet(
                    id,
                    type,
                    statistic,
                    materialTarget,
                    entityTarget,
                    helmet,
                    chestplate,
                    leggings,
                    boots
            ));
        }

        return sets;
    }
}