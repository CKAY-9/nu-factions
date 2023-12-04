package dev.ckay9.nu_factions.Tasks;

import dev.ckay9.nu_factions.Data;
import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Faction;

public class ClaimDecay {
  NuFactions factions;
  int task_id = 0;

  public ClaimDecay(NuFactions factions) {
    this.factions = factions;
    task_id = this.factions.getServer().getScheduler().scheduleSyncRepeatingTask(this.factions, () -> {
      for (int i = 0; i < this.factions.factions.size(); i++) {
        Faction faction = this.factions.factions.get(i);
        faction.deletePowerlessClaims();
      }
    }, 0L, 20L * 60 * Data.config_data.getInt("config.claim_decay_in_minutes", 30));
  }
}
