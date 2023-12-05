package dev.ckay9.nu_factions.Tasks;

import org.bukkit.Bukkit;

import dev.ckay9.nu_factions.Data;
import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Faction;
import dev.ckay9.nu_factions.Utils.Utils;

public class IntervalPower {
  NuFactions factions;
  int task_id = 0;

  public IntervalPower(NuFactions factions) {
    this.factions = factions;

    task_id = this.factions.getServer().getScheduler().scheduleSyncRepeatingTask(this.factions, () -> {
      int to_add = Data.config_data.getInt("config.interval_power_reward");
      for (int i = 0; i < this.factions.factions.size(); i++) {
        Faction faction = this.factions.factions.get(i);
        faction.faction_power += to_add;
        faction.saveFactionData();
      }
      Bukkit.broadcastMessage(Utils.formatText("&5All factions have been rewarded " + to_add + " power!")); 
    }, 0L, 20L * 60 * Data.config_data.getInt("config.interval_power_time_in_minutes", 30));
  }
}
