package me.mouseman.mousessets.listeners;

import me.mouseman.mousessets.managers.SetManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final SetManager setManager;

    public BlockBreakListener(SetManager setManager) {
        this.setManager = setManager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        setManager.handleBlockBreak(
                event.getPlayer(),
                event.getBlock().getType()
        );
    }
}