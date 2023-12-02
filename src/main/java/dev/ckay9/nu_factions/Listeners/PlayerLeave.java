package dev.ckay9.nu_factions.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Faction;

public class PlayerLeave implements Listener {
  NuFactions factions;

  public PlayerLeave(NuFactions factions) {
    this.factions = factions;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerLeave(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    Faction f = Faction.getFactionFromMemberUUID(this.factions, player, false);
    f.active_members.remove(player);
  }
}
