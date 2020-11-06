package me.ponktacology.brewmaster.util;

import org.bukkit.ChatColor;

public class ColorUtil {
    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
