package dev.ckay9.nu_factions.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Faction;

public class PlayerJoin implements Listener {
  public NuFactions factions;

  public PlayerJoin(NuFactions factions) {
    this.factions = factions;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    Faction.getFactionFromMemberUUID(this.factions, player, true);
  }
}
