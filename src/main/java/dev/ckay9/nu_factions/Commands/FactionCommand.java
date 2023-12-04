package dev.ckay9.nu_factions.Commands;

import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Claim;
import dev.ckay9.nu_factions.Factions.Faction;
import dev.ckay9.nu_factions.Utils.Utils;
import dev.ckay9.nu_factions.Utils.Vector3;


public class FactionCommand implements CommandExecutor {
  public NuFactions factions;

  public FactionCommand(NuFactions factions) {
    this.factions = factions;
  }

  private void executeCreate(@Nullable Faction faction, Player player, String[] args) {
    if (faction != null) {
      player.sendMessage(Utils.formatText("&cYou are currently in a faction!"));
      return;
    }
    
    String faction_name = args[1];
    ArrayList<UUID> uuids = new ArrayList<UUID>();
    uuids.add(player.getUniqueId());
    Faction new_faction = new Faction(new ArrayList<Claim>(), uuids, faction_name, player.getUniqueId(), 100);
    new_faction.saveFactionData();
    this.factions.factions.add(new_faction);
    player.sendMessage(Utils.formatText("&aSuccesfully created faction " + faction_name));
  }

  private void executeInformation(Player player, Faction faction) {
    player.sendMessage(Utils.formatText("&aYour Current Faction: " + faction.faction_name));
    player.sendMessage(Utils.formatText("&9Current Power Level: " + faction.faction_power));
    if (faction != null & faction.active_members != null) {
      player.sendMessage(Utils.formatText("&aActive Members: "));
      for (int i = 0; i < faction.active_members.size(); i++) {
        player.sendMessage(Utils.formatText("&a - " + faction.active_members.get(i).getName()));
      }
    }
  }

  private void executeClaim(Player player, String[] args, Faction faction) {
    String type = args[1].toLowerCase();

    if (type.contains("new")) {
      if (faction.faction_leader.toString().equalsIgnoreCase(player.getUniqueId().toString())) {
        player.sendMessage(Utils.formatText("&cYou need to be the faction leader to execute this command!"));
        return;
      }

      String claim_name = args[2];
      int claim_radius = Integer.parseInt(args[3]);
      int required_power = (int)Math.floor(claim_radius / (10 * (faction.faction_claims.size() / 10)));

      if (required_power > faction.faction_power) {
        player.sendMessage(Utils.formatText("&cYour faction doesn't have enough power to create this claim!"));
        return;
      }

      faction.faction_power -= required_power;

      Location starting_location = player.getLocation().add(claim_radius, 320, claim_radius);
      Location ending_location = player.getLocation().add(-claim_radius, -64, -claim_radius);

      boolean collides_with_others = Claim.doesClaimCollideWithOthers(starting_location, ending_location, this.factions);
      if (collides_with_others) {
        player.sendMessage(Utils.formatText("&cThis claim runs into already established claims!"));
        return;
      }

      Claim new_claim = new Claim(
        new Vector3(starting_location.getBlockX(), 320, starting_location.getBlockZ()), 
        new Vector3(ending_location.getBlockX(), -64, ending_location.getBlockZ()), 
        claim_name);
      faction.faction_claims.add(new_claim);
      faction.saveFactionData();
    }
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return false;
    }

    try {
      String subcommand = args[0].toLowerCase();
      Player player = (Player) sender;
      Faction faction = Faction.getFactionFromMemberUUID(this.factions, player, false);

      if (subcommand.contains("create")) {
        executeCreate(faction, player, args);
        return false;
      }

      if (subcommand.contains("info")) {
        executeInformation(player, faction);
        return false;
      }

      if (subcommand.contains("claim")) {
        executeClaim(player, args, faction);
        return false;
      }

      return false;
    } catch (Exception ex) {
      sender.sendMessage(Utils.formatText("&c" + ex.toString()));
      Utils.getPlugin().getLogger().warning(ex.toString());
    }

    return false;
  }
}
