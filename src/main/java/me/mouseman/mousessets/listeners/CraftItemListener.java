package me.mouseman.mousessets.listeners;

import me.mouseman.mousessets.managers.SetManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class CraftItemListener implements Listener {

    private final SetManager setManager;

    public CraftItemListener(SetManager setManager) {
        this.setManager = setManager;
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {

        ItemStack result = event.getRecipe().getResult();

        if (result == null) return;

        setManager.handleItemCraft(
                (org.bukkit.entity.Player) event.getWhoClicked(),
                result.getType()
        );
    }
}