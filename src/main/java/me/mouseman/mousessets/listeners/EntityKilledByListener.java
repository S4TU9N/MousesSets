package me.mouseman.mousessets.listeners;

import me.mouseman.mousessets.managers.SetManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class EntityKilledByListener implements Listener {

    private final SetManager setManager;

    public EntityKilledByListener(SetManager setManager) {
        this.setManager = setManager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player victim = event.getPlayer();

        EntityDamageEvent cause = victim.getLastDamageCause();
        if (cause == null) return;

        Entity killer = null;

        if (cause.getEntity() instanceof Player p) {
            killer = p;
        } else if (cause.getEntity() instanceof Entity e) {
            killer = e;
        }
    
        if (killer == null) return;

        setManager.handleEntityKilledBy(victim, killer);
    }
}