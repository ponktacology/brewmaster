package me.ponktacology.brewmaster.brewer.listener;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import me.ponktacology.brewmaster.brewer.BrewerHelper;
import me.ponktacology.brewmaster.brewer.type.BrewerType;
import me.ponktacology.brewmaster.util.ColorUtil;
import me.ponktacology.simpleconfig.config.annotation.Configurable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class BrewerEggInteractListener implements Listener {

    @Configurable
    public static String wrongTerritoryMessage = "&cYou can only spawn brewers on your own territory.";

    @Configurable
    public static String mustBeInFactionMessage = "&cYou must be in faction in order to spawn brewers.";

    private final BrewerHelper brewerHelper;

    public BrewerEggInteractListener(BrewerHelper brewerHelper) {
        this.brewerHelper = brewerHelper;
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = event.getItem();
        if (item == null) return;

        if (!brewerHelper.isBrewerEgg(item)) return;

        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);

        if(!fPlayer.hasFaction()) {
            player.sendMessage(ColorUtil.color(mustBeInFactionMessage));
            return;
        }

        if (!fPlayer.isInOwnTerritory()) {
            player.sendMessage(ColorUtil.color(wrongTerritoryMessage));
            return;
        }

        BrewerType brewerType = brewerHelper.getBrewerType(item);

        if (brewerType == null) {
            player.sendMessage(ColorUtil.color("&cInvalid egg, please contact staff."));
            return;
        }

        if (item.getAmount() == 1) {
            player.setItemInHand(null);
        } else {
            item.setAmount(item.getAmount() - 1);
        }

        brewerHelper.spawnBrewer(event.getClickedBlock().getLocation().add(0, 1, 0), brewerType);
    }
}
