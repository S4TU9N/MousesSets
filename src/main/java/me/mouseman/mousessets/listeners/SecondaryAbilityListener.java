package me.mouseman.mousessets.listeners;

import me.mouseman.mousessets.abilities.AbilityManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SecondaryAbilityListener implements Listener {

    private final AbilityManager abilityManager;

    public SecondaryAbilityListener(AbilityManager abilityManager) {
        this.abilityManager = abilityManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if (event.getAction() != Action.LEFT_CLICK_AIR) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.isSneaking()) {
            return;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (hand.getType() != Material.AIR) {
            return;
        }

        if (!hasFullSet(player)) {
            return;
        }

        event.setCancelled(true);

        abilityManager.activate(player, "dash"); // example secondary ability
    }

    private boolean hasFullSet(Player player) {
        return true; // hook into SetManager later
    }
}   