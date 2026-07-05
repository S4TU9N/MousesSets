package me.mouseman.mousessets.abilities;

import me.mouseman.mousessets.definitions.AbilityDefinition;
import me.mouseman.mousessets.definitions.AbilityResult;
import org.bukkit.entity.Player;

public interface Ability {

    /**
     * Unique ID.
     * Must match the ID in abilities.yml.
     */
    String getId();

    /**
     * Runs the ability.
     */
    AbilityResult activate(Player player, AbilityDefinition definition);
}