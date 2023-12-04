package dev.ckay9.nu_factions.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import dev.ckay9.nu_factions.Data;
import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Claim;
import dev.ckay9.nu_factions.Factions.Faction;
import dev.ckay9.nu_factions.Factions.FactionClaim;
import dev.ckay9.nu_factions.Utils.Utils;

import org.bukkit.event.EventPriority;

public class EntityDeath implements Listener {
  NuFactions factions;

  public EntityDeath(NuFactions factions) {
    this.factions = factions;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onEntityDeath(EntityDeathEvent event) {
    if (!(event.getEntity().getKiller() instanceof Player)) {
      return;
    }

    Player player = event.getEntity().getKiller();
    Faction faction = Faction.getFactionFromMemberUUID(this.factions, player, false);
    if (faction == null) {
      return;
    }

    FactionClaim current_claim = Claim.getCurrentClaim(player.getLocation(), this.factions);
    if (current_claim == null) {
      return;
    }

    if (current_claim.faction != faction) { 
      return;
    }

    int to_add = Data.config_data.getInt("config.entity_power_reward", 5);
    faction.faction_power += to_add;
    faction.saveFactionData();
    player.sendMessage(Utils.formatText("&aYou have gained " + to_add + " power for your faction!"));
  }
}
