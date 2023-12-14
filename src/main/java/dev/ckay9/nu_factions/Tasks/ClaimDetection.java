package dev.ckay9.nu_factions.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Claim;
import dev.ckay9.nu_factions.Factions.FactionClaim;
import dev.ckay9.nu_factions.Utils.Utils;

public class ClaimDetection {
  public ClaimDetection(NuFactions factions) {
    factions.getServer().getScheduler().scheduleSyncRepeatingTask(factions, () -> {
      for (Player player : Bukkit.getOnlinePlayers()) {
        FactionClaim current_claim = Claim.getCurrentClaim(player.getLocation(), factions);
        if (current_claim == null) {
          // this checks if a player was previously inside a claim
          FactionClaim current_player_claim = Claim.getCurrentPlayerClaim(player, factions);
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
    }, 0, 20L);
    
  }
}
