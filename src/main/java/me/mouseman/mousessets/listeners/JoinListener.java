package me.mouseman.mousessets.listeners;

import me.mouseman.mousessets.managers.SetManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener {
    private final SetManager setManager;

    public JoinListener(SetManager setManager) {
        this.setManager = setManager;
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        setManager.updatePlayerSet(event.getPlayer());
    }
}
