package me.mouseman.mousessets.listeners;

import me.mouseman.mousessets.managers.SetManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class ArmorChangeListener implements Listener {

    private final SetManager setManager;

    public ArmorChangeListener(SetManager setManager) {
        this.setManager = setManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;

        // Delay 1 tick so armor updates BEFORE evaluation
        player.getServer().getScheduler().runTask(
                setManager.getPlugin(),
                () -> setManager.updatePlayerSet(player)
        );
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;

        player.getServer().getScheduler().runTask(
                setManager.getPlugin(),
                () -> setManager.updatePlayerSet(player)
        );
    }
}