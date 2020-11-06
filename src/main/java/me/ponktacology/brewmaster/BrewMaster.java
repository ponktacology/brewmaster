package me.ponktacology.brewmaster;

import me.ponktacology.brewmaster.brewer.BrewerHelper;
import me.ponktacology.brewmaster.brewer.listener.BrewerEggInteractListener;
import me.ponktacology.brewmaster.brewer.listener.BrewerInteractListener;
import me.ponktacology.brewmaster.command.SpawnBrewerCommand;
import me.ponktacology.simpleconfig.config.ConfigFactory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BrewMaster extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new ConfigFactory(this.getClass());
        BrewerHelper brewerHelper = new BrewerHelper();

        Bukkit.getPluginManager().registerEvents(new BrewerEggInteractListener(brewerHelper), this);
        Bukkit.getPluginManager().registerEvents(new BrewerInteractListener(this, brewerHelper), this);
        getCommand("brewereggs").setExecutor(new SpawnBrewerCommand(brewerHelper));
    }
}