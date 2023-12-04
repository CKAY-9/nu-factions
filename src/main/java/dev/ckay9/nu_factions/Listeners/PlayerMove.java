package dev.ckay9.nu_factions.Listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Claim;
import dev.ckay9.nu_factions.Factions.FactionClaim;
import dev.ckay9.nu_factions.Utils.Utils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class PlayerMove implements Listener {
  NuFactions factions;

  public PlayerMove(NuFactions factions) {
    this.factions = factions;
  }

  @EventHandler(priority = EventPriority.LOWEST) 
  public void onPlayerMovement(PlayerMoveEvent event) {
    Player player = event.getPlayer(); 
    FactionClaim current_claim = Claim.getCurrentClaim(player.getLocation(), this.factions);
    if (current_claim == null) {
      // this checks if a player was previously inside a claim
      FactionClaim current_player_claim = Claim.getCurrentPlayerClaim(player, this.factions);
      if (current_player_claim == null) {
        return;
      }
      player.sendTitle(
        Utils.formatText("&5&lNow Leaving " + current_player_claim.claim.claim_name),
        Utils.formatText("&5Controlled by " + current_player_claim.faction.faction_name), 
        5, 
        60, 
        5
      );
      current_player_claim.claim.players_inside.remove(player);
      return;
    }

    if (!current_claim.claim.isPlayerInside(player)) {
      player.sendTitle(
        Utils.formatText("&5&lNow Entering " + current_claim.claim.claim_name),
        Utils.formatText("&5Controlled by " + current_claim.faction.faction_name), 
        5, 
        60, 
        5
      );
      current_claim.claim.players_inside.add(player);
    }
  }
}
