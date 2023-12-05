package dev.ckay9.nu_factions.Listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import dev.ckay9.nu_factions.Data;
import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Claim;
import dev.ckay9.nu_factions.Factions.Faction;
import dev.ckay9.nu_factions.Factions.FactionClaim;
import dev.ckay9.nu_factions.Utils.Utils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class PlayerDeath implements Listener {
  public NuFactions factions;

  public PlayerDeath(NuFactions factions) {
    this.factions = factions;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerDeath(PlayerDeathEvent event) {
    Player killer = event.getEntity().getKiller();
    Player player = event.getEntity().getPlayer();

    if (player == null || killer == null) {
      return;
    }

    Faction killer_faction = Faction.getFactionFromMemberUUID(this.factions, killer, false);
    if (killer_faction == null) {
      return;
    }

    FactionClaim current_claim = Claim.getCurrentClaim(player.getLocation(), this.factions);
    if (current_claim == null || current_claim.faction.faction_leader != killer_faction.faction_leader) {
      return;
    }

    killer_faction.faction_power += Data.config_data.getInt("config.player_power_reward", 20);
    killer.sendMessage(Utils.formatText("&aYou gained 5 power to your faction!"));
  }
}
