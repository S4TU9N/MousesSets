package me.mouseman.mousessets.abilities;

import me.mouseman.mousessets.definitions.AbilityDefinition;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbilityLoader {

    private final JavaPlugin plugin;

    public AbilityLoader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public Map<String, AbilityDefinition> loadAbilities() {
        File file = new File(plugin.getDataFolder(), "abilities.yml");

        if (!file.exists()) {
            plugin.saveResource("abilities.yml", false);
        }

        FileConfiguration config =
                YamlConfiguration.loadConfiguration(file);

                Map<String, AbilityDefinition> abilities = new HashMap<>();
                
        for (String id : config.getKeys(false)) {

            ConfigurationSection section =
                    config.getConfigurationSection(id);

            if (section == null) {
                continue;
            }
            
            String displayName = section.getString("display.name", id);

            List<String> description = section.getStringList("description");

            int cooldown = section.getInt("cooldown", 0);
            
            Map<String, Object> settings = new HashMap<>();

            ConfigurationSection settingsSection =
                    section.getConfigurationSection("settings");

            if (settingsSection != null) {

                for (String key : settingsSection.getKeys(false)) {

                    settings.put(
                            key,
                            settingsSection.get(key)
                    );
                }
            }
            AbilityDefinition definition =
                    new AbilityDefinition(
                            id,
                            displayName,
                            description,
                            cooldown,
                            settings
                    );

            abilities.put(id, definition);
        }

        return abilities;
    }
    
}