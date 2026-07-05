package me.mouseman.mousessets.definitions;

import java.util.List;
import java.util.Map;

public class AbilityDefinition {

    private final String id;

    private final String displayName;
    private final List<String> description;

    private final int cooldown;

    private final Map<String, Object> settings;

    public AbilityDefinition(
            String id,
            String displayName,
            List<String> description,
            int cooldown,
            Map<String, Object> settings
    ) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.cooldown = cooldown;
        this.settings = settings;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getDescription() {
        return description;
    }

    public int getCooldown() {
        return cooldown;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }
}