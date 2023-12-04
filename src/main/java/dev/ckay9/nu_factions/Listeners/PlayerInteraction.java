package dev.ckay9.nu_factions.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.EventPriority;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Claim;
import dev.ckay9.nu_factions.Factions.Faction;
import dev.ckay9.nu_factions.Factions.FactionClaim;

public class PlayerInteraction implements Listener {
  NuFactions factions;

  public PlayerInteraction(NuFactions factions) {
    this.factions = factions;
  }

  private boolean canInteract(Player player) {
    Faction player_faction = Faction.getFactionFromMemberUUID(this.factions, player, false);
    FactionClaim current_claim = Claim.getCurrentClaim(player.getLocation(), this.factions);
    if (current_claim == null) {
      return true;
    }

    if (player_faction == current_claim.faction) {
      return true; 
    }

    return false;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onBlockBreak(BlockBreakEvent event) {
    event.setCancelled(!canInteract(event.getPlayer()));
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onBlockPlace(BlockPlaceEvent event) {
    event.setCancelled(!canInteract(event.getPlayer()));
  } 
}
