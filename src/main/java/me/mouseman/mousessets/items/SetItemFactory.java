package me.mouseman.mousessets.items;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.mouseman.mousessets.sets.MilestoneSet;

public class SetItemFactory {

    private final SetItemKeys keys;

    public SetItemFactory(SetItemKeys keys) {
        this.keys = keys;
    }

    public boolean isSetItem(ItemStack item, String setId) {

        if (item == null || !item.hasItemMeta()) return false;

        String id = item.getItemMeta()
                .getPersistentDataContainer()
                .get(keys.SET_ID, PersistentDataType.STRING);
        if (id == null) return false;
        return setId.equals(id);
    }

    public ItemStack create(ItemStack base, MilestoneSet set, String piece) {

        ItemMeta meta = base.getItemMeta();

        meta.getPersistentDataContainer().set(
                keys.SET_ID,
                PersistentDataType.STRING,
                set.getId()
        );

        meta.getPersistentDataContainer().set(
                keys.SET_PIECE,
                PersistentDataType.STRING,
                piece
        );

        base.setItemMeta(meta);

        return base;
    }
}