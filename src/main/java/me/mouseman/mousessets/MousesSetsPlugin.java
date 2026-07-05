package me.mouseman.mousessets;

import me.mouseman.mousessets.commands.MousesSetsCommand;
import me.mouseman.mousessets.sets.MilestoneSet;
import me.mouseman.mousessets.items.SetItemFactory;
import me.mouseman.mousessets.items.SetItemKeys;
import me.mouseman.mousessets.listeners.*;
import me.mouseman.mousessets.managers.ProgressService;
import me.mouseman.mousessets.managers.SetLoader;
import me.mouseman.mousessets.managers.SetManager;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class MousesSetsPlugin extends JavaPlugin {

    private SetManager setManager;
    private ProgressService progressService;

    @Override
    public void onEnable() {

        // 1. persistence layer (player progress storage)
        this.progressService = new ProgressService(this);

        // 2. load set definitions from sets.yml
        SetLoader loader = new SetLoader(this);
        Map<String, MilestoneSet> sets = loader.loadSets();

        // 3. keys used for item tagging (PDC)
        SetItemKeys keys = new SetItemKeys(this);

        // 4. main system manager
        this.setManager = new SetManager(
                this,
                progressService,
                sets,
                keys
        );

        // 5. command wiring
        if (getCommand("mousessets") != null) {
            getCommand("mousessets").setExecutor(
                    new MousesSetsCommand(setManager, loader)
            );
        }

        // 6. listeners
        var pm = getServer().getPluginManager();

        pm.registerEvents(new BlockBreakListener(setManager), this);
        pm.registerEvents(new ItemBreakListener(setManager), this);
        pm.registerEvents(new CraftItemListener(setManager), this);
        pm.registerEvents(new ItemUseListener(setManager), this);
        pm.registerEvents(new EntityKillListener(setManager), this);
        pm.registerEvents(new EntityKilledByListener(setManager), this);
        pm.registerEvents(new ArmorEquipListener(setManager), this);
    }

    public SetManager getSetManager() {
        return setManager;
    }
}