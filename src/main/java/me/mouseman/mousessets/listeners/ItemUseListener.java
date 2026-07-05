package me.mouseman.mousessets.listeners;

import me.mouseman.mousessets.managers.SetManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemUseListener implements Listener {

    private final SetManager setManager;

    public ItemUseListener(SetManager setManager) {
        this.setManager = setManager;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {

        if (event.getAction() == Action.PHYSICAL) return;

        Material item = event.getItem() != null
                ? event.getItem().getType()
                : null;

        if (item == null) return;

        setManager.handleItemUse(
                event.getPlayer(),
                item
        );
    }
}