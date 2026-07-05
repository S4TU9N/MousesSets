package me.mouseman.mousessets.listeners;

import me.mouseman.mousessets.managers.SetManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBreakListener implements Listener {

    private final SetManager setManager;

    public ItemBreakListener(SetManager setManager) {
        this.setManager = setManager;
    }

    @EventHandler
    public void onDamage(PlayerItemDamageEvent event) {

        ItemStack item = event.getItem();
        if (item == null || item.getType().isAir()) return;

        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof Damageable damageable)) return;

        int damage = damageable.getDamage();
        int max = item.getType().getMaxDurability();

        if (damage + event.getDamage() >= max) {

            setManager.handleItemBreak(
                    event.getPlayer(),
                    item.getType()
            );
        }
    }
}