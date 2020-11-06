package me.ponktacology.brewmaster.brewer.listener;

import me.ponktacology.brewmaster.brewer.BrewerHelper;
import me.ponktacology.brewmaster.brewer.type.BrewerType;
import me.ponktacology.brewmaster.util.ColorUtil;
import me.ponktacology.brewmaster.util.TimeUtil;
import me.ponktacology.simpleconfig.config.annotation.Configurable;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public class BrewerInteractListener implements Listener {

    private static final long ONE_DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1);

    @Configurable
    public static String delayMessage = "&cBrewer can brew only 64 potions per day, come back in {time}";

    private final JavaPlugin plugin;
    private final BrewerHelper brewerHelper;

    public BrewerInteractListener(JavaPlugin plugin, BrewerHelper brewerHelper) {
        this.plugin = plugin;
        this.brewerHelper = brewerHelper;
    }

    @EventHandler
    public void onInteractEvent(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity == null) return;

        if (!brewerHelper.isBrewer(entity)) return;
        event.setCancelled(true);

        Player player = event.getPlayer();
        ItemStack itemInHand = player.getItemInHand();

        if (itemInHand == null || itemInHand.getType() != Material.GLASS_BOTTLE) return;

        BrewerType brewerType = brewerHelper.getBrewerType(entity);

        if (!entity.hasMetadata("uses") || !entity.hasMetadata("waitUntil")) {
            entity.setMetadata("uses", new FixedMetadataValue(plugin, 0));
            entity.setMetadata("waitUntil", new FixedMetadataValue(plugin, 0));
        } else {
            long waitUntil = entity.getMetadata("waitUntil").get(0).asLong();

            if (System.currentTimeMillis() < waitUntil) {
                player.sendMessage(ColorUtil.color(delayMessage.replace("{time}", TimeUtil.millisToRoundedTime(waitUntil - System.currentTimeMillis()))));
                entity.setMetadata("uses", new FixedMetadataValue(plugin, 0));
                return;
            }

            int uses = entity.getMetadata("uses").get(0).asInt();

            if (uses >= 64) {
                entity.setMetadata("waitUntil", new FixedMetadataValue(plugin, System.currentTimeMillis() + ONE_DAY_IN_MILLIS));
                player.sendMessage(ColorUtil.color(delayMessage.replace("{time}", TimeUtil.millisToRoundedTime(ONE_DAY_IN_MILLIS))));
                return;
            }

            entity.setMetadata("uses", new FixedMetadataValue(plugin, uses + 1));
        }

        if (itemInHand.getAmount() == 1) {
            player.setItemInHand(brewerType.getItemStack().clone());
        } else {
            itemInHand.setAmount(itemInHand.getAmount() - 1);

            if (player.getInventory().firstEmpty() == -1) {
                player.getWorld().dropItemNaturally(player.getLocation(), brewerType.getItemStack().clone());
            } else {
                player.getInventory().addItem(brewerType.getItemStack().clone());
            }
        }
    }
}
