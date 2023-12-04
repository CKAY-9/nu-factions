package dev.ckay9.nu_factions;

import java.util.ArrayList;
import org.bukkit.plugin.java.JavaPlugin;

import dev.ckay9.nu_factions.Commands.FactionCommand;
import dev.ckay9.nu_factions.Commands.FactionCompletor;
import dev.ckay9.nu_factions.Factions.Faction;
import dev.ckay9.nu_factions.Listeners.PlayerDeath;
import dev.ckay9.nu_factions.Listeners.PlayerInteraction;
import dev.ckay9.nu_factions.Listeners.PlayerJoin;
import dev.ckay9.nu_factions.Listeners.PlayerLeave;
import dev.ckay9.nu_factions.Listeners.PlayerMove;

public class NuFactions extends JavaPlugin {
  public ArrayList<Faction> factions = new ArrayList<Faction>();
  
  // Runs when plugin starts
  @Override
  public void onEnable() {
    Data.initializeDataFiles(); 
    factions = Faction.generateFromSaveFile(Data.factions_data);

    // Commands
    this.getCommand("nufaction").setExecutor(new FactionCommand(this));
    this.getCommand("nufaction").setTabCompleter(new FactionCompletor(this));

    // Listeners
    this.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
    this.getServer().getPluginManager().registerEvents(new PlayerLeave(this), this);
    this.getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
    this.getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
    this.getServer().getPluginManager().registerEvents(new PlayerInteraction(this), this);
  }

  @Override
  public void onDisable() {
    this.getLogger().info("Saving factions data...");
    for (int i = 0; i < factions.size(); i++) {
      Faction faction = factions.get(i);
      faction.saveFactionData();
    }
    this.getLogger().info("Saved faction data!");
  }
}
