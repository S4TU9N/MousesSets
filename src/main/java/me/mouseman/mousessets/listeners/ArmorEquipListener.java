package me.mouseman.mousessets.listeners;

import me.mouseman.mousessets.managers.SetManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorEquipListener implements Listener {

    private final SetManager setManager;

    public ArmorEquipListener(SetManager setManager) {
        this.setManager = setManager;
    }

    @EventHandler
    public void onRightClickArmor(PlayerInteractEvent event) {

        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null) return;
        if (!item.getType().name().endsWith("_HELMET")
                && !item.getType().name().endsWith("_CHESTPLATE")
                && !item.getType().name().endsWith("_LEGGINGS")
                && !item.getType().name().endsWith("_BOOTS")) {
            return;
        }

    
        player.getServer().getScheduler().runTask(
                setManager.getPlugin(),
                () -> setManager.updatePlayerSet(player)
        );
    }
}