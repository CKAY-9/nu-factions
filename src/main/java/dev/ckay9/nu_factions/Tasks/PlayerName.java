package dev.ckay9.nu_factions.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Faction;
import dev.ckay9.nu_factions.Utils.Utils;

public class PlayerName {
  NuFactions factions;
  int task_id = 0;

  public PlayerName(NuFactions factions) {
    this.factions = factions;
    
    this.task_id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Utils.getPlugin(), () -> {
      for (Player player : Bukkit.getOnlinePlayers()) {
        Faction faction = Faction.getFactionFromMemberUUID(this.factions, player, false);
        if (faction == null) {
          continue;
        }

        player.setDisplayName(Utils.formatText("&5[" + faction.faction_name + "] &r" + player.getName())); 
        player.setPlayerListName(Utils.formatText("&5[" + faction.faction_name + "] &r" + player.getName())); 
      }
    }, 0L, 20L);
  }
}
