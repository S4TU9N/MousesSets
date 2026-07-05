package me.mouseman.mousessets.abilities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.mouseman.mousessets.definitions.AbilityResult;
import me.mouseman.mousessets.definitions.RegisteredAbility;

public class AbilityManager {

    private final AbilityRegistry registry;

    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public AbilityManager(AbilityRegistry registry) {
        this.registry = registry;
    }

    public AbilityResult activate(Player player, String abilityId) {

        RegisteredAbility ability =
                registry.get(abilityId);

        if (ability == null) {
            return AbilityResult.NOT_FOUND;
        }

        return activate(player, ability);
    }

    public AbilityResult activate(
            Player player,
            RegisteredAbility ability
    ) {

        String id = ability.getDefinition().getId();

        if (isOnCooldown(player, id)) {
            return AbilityResult.ON_COOLDOWN;
        }

        AbilityResult result =
                ability.getImplementation().activate(
                        player,
                        ability.getDefinition()
                );

        if (result == AbilityResult.SUCCESS) {

            startCooldown(
                    player,
                    id,
                    ability.getDefinition().getCooldown()
            );
        }

        return result;
    }
    
    private boolean isOnCooldown(Player player, String abilityId) {

        Map<String, Long> playerCooldowns =
                cooldowns.get(player.getUniqueId());

        if (playerCooldowns == null) {
            return false;
        }

        Long cooldownEnd = playerCooldowns.get(abilityId);

        if (cooldownEnd == null) {
            return false;
        }

        if (cooldownEnd <= System.currentTimeMillis()) {

            playerCooldowns.remove(abilityId);

            if (playerCooldowns.isEmpty()) {
                cooldowns.remove(player.getUniqueId());
            }

            return false;
        }

        return true;
    }

    public boolean isOnCooldown(
            Player player,
            RegisteredAbility ability
    ) {
        return isOnCooldown(
                player,
                ability.getDefinition().getId()
        );
    }

    private void startCooldown(Player player, String abilityId, int seconds) {

        cooldowns
                .computeIfAbsent(
                        player.getUniqueId(),
                        k -> new HashMap<>()
                )
                .put(
                        abilityId,
                        System.currentTimeMillis() + (seconds * 1000L)
                );
    }

    public long getRemainingCooldown(Player player, String abilityId) {

        if (!isOnCooldown(player, abilityId)) {
            return 0;
        }

        long cooldownEnd = cooldowns
                .get(player.getUniqueId())
                .get(abilityId);

        return Math.max(
                0,
                (cooldownEnd - System.currentTimeMillis()) / 1000
        );
    }

    public long getRemainingCooldown(
            Player player,
            RegisteredAbility ability
    ) {
        return getRemainingCooldown(
                player,
                ability.getDefinition().getId()
        );
    }

    public void clearCooldown(Player player, String abilityId) {

        Map<String, Long> playerCooldowns =
                cooldowns.get(player.getUniqueId());

        if (playerCooldowns == null) {
            return;
        }

        playerCooldowns.remove(abilityId);

        if (playerCooldowns.isEmpty()) {
            cooldowns.remove(player.getUniqueId());
        }
    }

    public void clearCooldowns(Player player) {

        cooldowns.remove(player.getUniqueId());
    }





}
