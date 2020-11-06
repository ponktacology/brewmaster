package me.ponktacology.brewmaster.command;

import me.ponktacology.brewmaster.brewer.BrewerHelper;
import me.ponktacology.brewmaster.brewer.type.BrewerType;
import me.ponktacology.brewmaster.util.ColorUtil;
import me.ponktacology.simpleconfig.config.annotation.Configurable;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnBrewerCommand implements CommandExecutor {

    @Configurable
    public static String commandPermission = "brewereggs.admin";

    @Configurable
    public static String noPermissionMessage = "&cNo permission";

    private final BrewerHelper brewerHelper;

    public SpawnBrewerCommand(BrewerHelper brewerHelper) {
        this.brewerHelper = brewerHelper;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission(commandPermission)) {
            sender.sendMessage(ColorUtil.color(noPermissionMessage));
            return false;
        }

        if (args.length != 3) {
            sender.sendMessage(ColorUtil.color("&cUsage: /brewereggs <player> <type, (instant_health, strength, speed, fire_resistance)> <amount>"));
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(ColorUtil.color("&cPlayer is not online."));
            return false;
        }

        BrewerType brewerType;
        try {
            brewerType = BrewerType.valueOf(args[1].toUpperCase());
        } catch (Exception e) {
            sender.sendMessage(ColorUtil.color("&cInvalid brewer type."));
            return false;
        }

        int amount = 0;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (Exception e) {
            sender.sendMessage(ColorUtil.color("&cInvalid amount."));
            return false;
        }

        target.getInventory().addItem(brewerHelper.getBrewerEgg(amount, brewerType));
        sender.sendMessage(ColorUtil.color("&aSuccessfully given this player brewer eggs."));
        return false;
    }
}
