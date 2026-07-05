package me.mouseman.mousessets.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.mouseman.mousessets.abilities.AbilityManager;

public class PrimaryAbilityListener implements Listener {

    private final AbilityManager abilityManager;

    public PrimaryAbilityListener(AbilityManager abilityManager) {
        this.abilityManager = abilityManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR &&
            action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.isSneaking()) {
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != Material.AIR) {
            return;
        }

        if (!hasFullSet(player)) {
            return;
        }

        event.setCancelled(true);

        abilityManager.activate(player, "fireball"); // later: dynamic from set
    }

    private boolean hasFullSet(Player player) {
        // placeholder for SetManager integration
        return true;
    }
}