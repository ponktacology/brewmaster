package me.ponktacology.brewmaster.brewer.type;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum BrewerType {

    INSTANT_HEALTH("Instant Health II", new ItemStack(Material.POTION, 1, (short) 16421)),
    SPEED("Speed II", new ItemStack(Material.POTION, 1, (short) 8226)),
    STRENGTH("Strength I", new ItemStack(Material.POTION, 1, (short) 8201)),
    FIRE_RESISTANCE("Fire Resistance", new ItemStack(Material.POTION, 1, (short) 8259));

    private final String displayName;
    private final ItemStack itemStack;

    BrewerType(String displayName, ItemStack itemStack) {
        this.displayName = displayName;
        this.itemStack = itemStack;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
