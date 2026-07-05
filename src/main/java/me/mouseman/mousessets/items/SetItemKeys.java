package me.mouseman.mousessets.items;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class SetItemKeys {

    public final NamespacedKey SET_ID;
    public final NamespacedKey SET_PIECE;

    public SetItemKeys(JavaPlugin plugin) {
        this.SET_ID = new NamespacedKey(plugin, "set_id");
        this.SET_PIECE = new NamespacedKey(plugin, "set_piece");
    }
}