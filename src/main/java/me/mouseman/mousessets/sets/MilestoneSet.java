package me.mouseman.mousessets.sets;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

import me.mouseman.mousessets.definitions.TriggerType;

public class MilestoneSet {

    private final String id;

    private final TriggerType triggerType;
    private final Statistic statistic;
    
    private final Material materialTarget;
    private final EntityType entityTarget;

    private final int helmet;
    private final int chestplate;
    private final int leggings;
    private final int boots;

    public MilestoneSet(
            String id,
            TriggerType triggerType,
            Statistic statistic,
            Material materialTarget,
            EntityType entityTarget,
            int helmet,
            int chestplate,
            int leggings,
            int boots
    ) {
        this.id = id;
        this.triggerType = triggerType;
        this.statistic = statistic;
        this.materialTarget = materialTarget;
        this.entityTarget = entityTarget;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public String getId() {
        return id;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public Statistic getStatistic() {
        return statistic;
    }

    public Material getMaterialTarget() {
        return materialTarget;
    }

    public EntityType getEntityTarget() {
        return entityTarget;
    }

    public int getRequirement(int stage) {
        return switch (stage) {
            case 1 -> helmet;
            case 2 -> chestplate;
            case 3 -> leggings;
            case 4 -> boots;
            default -> -1;
        };
    }
}