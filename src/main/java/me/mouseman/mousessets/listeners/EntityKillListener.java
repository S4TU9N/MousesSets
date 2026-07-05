package me.mouseman.mousessets.listeners;

import me.mouseman.mousessets.managers.SetManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityKillListener implements Listener {

    private final SetManager setManager;

    public EntityKillListener(SetManager setManager) {
        this.setManager = setManager;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {

        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        Entity entity = event.getEntity();

        setManager.handleEntityKill(
                killer,
                entity.getType()
        );
    }
}