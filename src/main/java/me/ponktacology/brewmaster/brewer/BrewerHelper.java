package me.ponktacology.brewmaster.brewer;

import me.ponktacology.brewmaster.brewer.type.BrewerType;
import me.ponktacology.brewmaster.util.ColorUtil;
import me.ponktacology.simpleconfig.config.annotation.Configurable;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BrewerHelper {

    @Configurable(path = "brewer.name.instant_health")
    public static String brewerNameInstantHealth = "&cHealthy Brewer";

    @Configurable(path = "brewer.name.speed")
    public static String brewerNameSpeed = "&cFast Brewer";

    @Configurable(path = "brewer.name.strength")
    public static String brewerNameStrength = "&cStrong Brewer";


    @Configurable(path = "brewer.name.fire_resistance")
    public static String brewerNameFireResistance = "&cFire Brewer";

    @Configurable(path = "brewer.health")
    public static double health = 20.0;

    @Configurable(path = "egg.name")
    public static String brewerEggName = "&eBrewer Egg";

    @Configurable(path = "egg.lore")
    public static List<String> brewerEggLore = Arrays.asList("&7This is brewer egg", "&fType: {type}");

    @Configurable(path = "egg.enchant")
    public static boolean enchant = true;


    public Villager spawnBrewer(Location location, BrewerType type) {
        Villager villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);

        String name = "";

        switch (type) {
            case SPEED:
                name = brewerNameSpeed;
                break;
            case STRENGTH:
                name = brewerNameStrength;
                break;
            case INSTANT_HEALTH:
                name = brewerNameInstantHealth;
                break;
            case FIRE_RESISTANCE:
                name = brewerNameFireResistance;
                break;
        }

        villager.setCustomName(ColorUtil.color(name));
        villager.setCustomNameVisible(true);
        villager.setRemoveWhenFarAway(false);
        villager.setMaxHealth(health);
        villager.setHealth(health);
        villager.setBreed(false);
        villager.setProfession(Villager.Profession.PRIEST);
        freezeEntity(villager);
        return villager;
    }

    public void freezeEntity(Entity entity) {
        net.minecraft.server.v1_8_R3.Entity nmsEn = ((CraftEntity) entity).getHandle();
        NBTTagCompound compound = new NBTTagCompound();
        nmsEn.c(compound);
        compound.setByte("NoAI", (byte) 1);
        nmsEn.f(compound);
    }

    public ItemStack getBrewerEgg(int amount, BrewerType type) {
        ItemStack itemStack = new ItemStack(Material.MONSTER_EGG, amount);

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ColorUtil.color(brewerEggName));
        meta.setLore(brewerEggLore.stream().map(ColorUtil::color).map(line -> line.replace("{type}", type.getDisplayName())).collect(Collectors.toList()));
        itemStack.setItemMeta(meta);

        if (enchant) {
            itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        }

        return itemStack;
    }

    public boolean isBrewerEgg(ItemStack item) {
        if (item == null) return false;
        if (item.getType() != Material.MONSTER_EGG) return false;
        if (!item.hasItemMeta()) return false;
        if (!item.getItemMeta().getDisplayName().equals(ColorUtil.color(brewerEggName))) return false;

        if (enchant) {
            return item.getEnchantmentLevel(Enchantment.DURABILITY) == 10;
        }

        return true;
    }

    public BrewerType getBrewerType(ItemStack item) {
        for (String loreLine : item.getItemMeta().getLore()) {
            for (BrewerType value : BrewerType.values()) {
                if (loreLine.contains(value.getDisplayName())) {
                    return value;
                }
            }
        }

        return null;
    }

    public BrewerType getBrewerType(Entity entity) {
        String entityName = entity.getCustomName();

        if (entityName.equals(ColorUtil.color(brewerNameInstantHealth))) {
            return BrewerType.INSTANT_HEALTH;
        }

        if (entityName.equals(ColorUtil.color(brewerNameStrength))) {
            return BrewerType.STRENGTH;
        }

        if (entityName.equals(ColorUtil.color(brewerNameSpeed))) {
            return BrewerType.SPEED;
        }

        if (entityName.equals(ColorUtil.color(brewerNameFireResistance))) {
            return BrewerType.FIRE_RESISTANCE;
        }

        return null;
    }

    public boolean isBrewer(Entity entity) {
        if (entity == null) return false;
        if (entity.getType() != EntityType.VILLAGER) return false;

        return (entity.getCustomName().equals(ColorUtil.color(brewerNameInstantHealth)) || entity.getCustomName().equals(ColorUtil.color(brewerNameFireResistance)) || entity.getCustomName().equals(ColorUtil.color(brewerNameSpeed)) || entity.getCustomName().equals(ColorUtil.color(brewerNameStrength)));
    }
}
