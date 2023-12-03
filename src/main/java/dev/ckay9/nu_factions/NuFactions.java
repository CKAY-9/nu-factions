package dev.ckay9.nu_factions;

import java.util.ArrayList;
import org.bukkit.plugin.java.JavaPlugin;

import dev.ckay9.nu_factions.Commands.FactionCommand;
import dev.ckay9.nu_factions.Commands.FactionCompletor;
import dev.ckay9.nu_factions.Factions.Faction;
import dev.ckay9.nu_factions.Listeners.PlayerJoin;
import dev.ckay9.nu_factions.Listeners.PlayerLeave;

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
  }
}
