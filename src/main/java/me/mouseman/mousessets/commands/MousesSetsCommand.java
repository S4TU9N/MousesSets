package me.mouseman.mousessets.commands;

import me.mouseman.mousessets.managers.SetManager;
import me.mouseman.mousessets.sets.MilestoneSet;
import me.mouseman.mousessets.managers.SetLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class MousesSetsCommand implements CommandExecutor {

    private final SetManager setManager;
    private final SetLoader setLoader;

    public MousesSetsCommand(SetManager setManager, SetLoader setLoader) {
        this.setManager = setManager;
        this.setLoader = setLoader;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("§7Usage: /" + label + " reload");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {

            Map<String, MilestoneSet> sets = setLoader.loadSets();

            setManager.reload(sets);

            sender.sendMessage("§aMousesSets reloaded.");
            return true;
        }

        sender.sendMessage("§cUnknown subcommand.");
        return true;
    }
}