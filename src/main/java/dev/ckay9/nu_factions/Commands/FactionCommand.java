package dev.ckay9.nu_factions.Commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.ckay9.nu_factions.Data;
import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Claim;
import dev.ckay9.nu_factions.Factions.Faction;
import dev.ckay9.nu_factions.Factions.GUI.Views;
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

    for (int i = 0; i < this.factions.factions.size(); i++) {
      Faction f = this.factions.factions.get(i);
      if (f.faction_name.equals(faction_name)) {
        player.sendMessage(Utils.formatText("&cThis faction name is already taken!"));
        return;
      }
    }

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
    if (!faction.isPlayerLeader(player)) {
      player.sendMessage(Utils.formatText("&cYou need to be the faction leader to execute this command!"));
      return;
    }
    
    String claim_name = args[2];
    if (type.contains("new")) {
      for (int i = 0; i < faction.faction_claims.size(); i++) {
        Claim claim = faction.faction_claims.get(i); 
        if (claim.claim_name.equals(claim_name)) {
          player.sendMessage(Utils.formatText("&cClaims must have unique names!"));
          return;
        }
      }

      int claim_radius = Integer.parseInt(args[3]);
      int required_power = (int)Math.floor(claim_radius / (1 - Data.config_data.getDouble("config.claim_addition_cost_percentage", 0.1)));

      if (required_power > faction.faction_power) {
        player.sendMessage(Utils.formatText("&cYour faction doesn't have enough power to create this claim!"));
        return;
      }

      faction.faction_power -= required_power;

      Location starting_location = player.getLocation().add(claim_radius, 320, claim_radius);
      Location ending_location = player.getLocation().add(-claim_radius, -64, -claim_radius);

      boolean collides_with_others = Claim.doesClaimCollideWithOthers(starting_location, ending_location, this.factions, null);
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

      player.sendMessage(Utils.formatText("&aSuccesfully created claim " + claim_name + "!"));
    }

    if (type.contains("delete")) {
      Claim claim = null;
      for (int i = 0; i < faction.faction_claims.size(); i++) {
        Claim c = faction.faction_claims.get(i);
        if (c.claim_name.equals(claim_name)) {
          claim = c;
        }
      }
      if (claim == null) {
        player.sendMessage(Utils.formatText("&cFailed to find claim with specified name!"));
        return;
      }
      faction.faction_claims.remove(claim);
      try {
        Data.factions_data.set(faction.faction_leader.toString() + ".claims." + claim_name, null);
        Data.factions_data.save(Data.factions_file);
        faction.saveFactionData();
        player.sendMessage(Utils.formatText("&aSuccesfully deleted claim " + claim_name));
      } catch (IOException ex) {
        player.sendMessage(Utils.formatText("&c" + ex.toString()));
        Utils.getPlugin().getLogger().warning(ex.toString());
      }
    }

    if (type.contains("change")) {
      Claim claim = null;
      for (int i = 0; i < faction.faction_claims.size(); i++) {
        Claim c = faction.faction_claims.get(i);
        if (c.claim_name.equals(claim_name)) {
          claim = c;
        }
      }
      if (claim == null) {
        player.sendMessage(Utils.formatText("&cFailed to find claim with specified name!"));
        return;
      }

      int new_radius = Integer.parseInt(args[3]);
      int cost = Claim.getCost(new_radius * 2);
      if (cost > faction.faction_power) {
        player.sendMessage(Utils.formatText("&cYour faction doesn't have enough power to do this change!"));
        return;
      }
      
      int prev_radius = (int)Math.round(claim.calculateSideLength() / 2);
      Location starting = new Location(
        player.getWorld(), 
        claim.starting_positon.x - prev_radius, 
        0, 
        claim.starting_positon.z - prev_radius
      ).add(new_radius, 320, new_radius);
      Location ending = new Location(
        player.getWorld(), 
        claim.starting_positon.x - prev_radius, 
        0, 
        claim.starting_positon.z - prev_radius
      ).add(-new_radius, -64, -new_radius);

      boolean does_collide = Claim.doesClaimCollideWithOthers(starting, ending, this.factions, claim);
      if (does_collide) {
        player.sendMessage(Utils.formatText("&cThis change will run into other claims!"));
        return;
      }

      claim.starting_positon = new Vector3(starting.getBlockX(), 320, starting.getBlockZ());
      claim.ending_position = new Vector3(ending.getBlockX(), -64, ending.getBlockZ());
      faction.saveFactionData();
      player.sendMessage(Utils.formatText("&aSuccesfully updated claim!"));
    }
  }

  private void executeDelete(Faction faction, Player player) {
    if (!faction.isPlayerLeader(player)) {
      player.sendMessage(Utils.formatText("&cYou must be a faction leader to execute this command!"));
      return;
    }

    faction.delete(this.factions);
    player.sendMessage(Utils.formatText("&aSuccesfully deleted your faction!"));
  }

  private void executeInvite(Faction faction, Player player, String[] args) {
    faction.invitePlayer(Bukkit.getPlayer(args[1]), player);    
  }

  private void executeJoin(Faction faction, Player player, String[] args) {
    if (faction != null) {
      player.sendMessage(Utils.formatText("&cYou must leave your current faction to join a new one: /nufaction leave"));
      return;
    }

    String faction_name = args[1];
    Faction target_faction = Faction.getFactionFromName(this.factions, faction_name);
    if (target_faction == null) {
      player.sendMessage(Utils.formatText("&cFailed to get faction with specified name!"));
      return;
    }
    target_faction.acceptInvite(player); 
  }

  private void executeLeaderboard(Faction faction, Player player) {
    LinkedHashMap<String, Long> sorted = Utils.getLeaderboard(this.factions); 
    player.sendMessage(Utils.formatText("&c&lTOP FACTIONS"));
    player.sendMessage(Utils.formatText("&c&l------------"));
    if (sorted.keySet().size() <= 0) {
      player.sendMessage(Utils.formatText("&aNO FACTIONS FOUND!"));
      return;
    }
    int index = 0;
    for (String key : sorted.keySet()) {
      if (index == 5) break; 
      player.sendMessage(Utils.formatText("&a " + (index + 1) + ". " + key + ", Power: " + sorted.get(key)));
      index++;
    }
  }

  private void executeAdmin(Player player, String[] args) {
    String type = args[1];
    String faction_name = args[2];
    Faction faction = Faction.getFactionFromName(this.factions, faction_name);
    if (faction == null) {
      player.sendMessage(Utils.formatText("&cFailed to find faction with specified name!"));
      return;
    }

    if (type.contains("give-power")) {
      long power_to_add = Long.parseLong(args[3]); 
      faction.faction_power += power_to_add;
      faction.saveFactionData();
      return;
    }

    if (type.contains("take-power")) {
      long power_to_remove = Long.parseLong(args[3]);
      faction.faction_power -= power_to_remove;
      faction.saveFactionData();
      return;
    }

    if (type.contains("set-power")) {
      long power_to_set = Long.parseLong(args[3]);
      faction.faction_power = power_to_set;
      faction.saveFactionData();
      return;
    }

    if (type.contains("delete-faction")) {
      faction.delete(this.factions);
    }
  }

  private void executeGUI(Player player, Faction faction) {
    Views.openNavigationMenu(player, faction);
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

      if (subcommand.length() <= 0 && args.length <= 0) {
        executeGUI(player, faction);
      }

      if (subcommand.contains("create")) {
        executeCreate(faction, player, args);
        return false;
      }

      if (subcommand.contains("delete")) {
        executeDelete(faction, player);
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

      if (subcommand.contains("invite")) {
        executeInvite(faction, player, args);
        return false;
      }

      if (subcommand.contains("join")) {
        executeJoin(faction, player, args);
        return false;
      }

      if (subcommand.contains("leaderboard")) {
        executeLeaderboard(faction, player);
        return false;
      }

      if (subcommand.contains("admin")) {
        executeAdmin(player, args);
        return false;
      }

      if (subcommand.contains("gui")) {
        executeGUI(player, faction);
        return false;
      }

      return false;
    } catch (Exception ex) {
      if (sender instanceof Player) {
        Faction f = Faction.getFactionFromMemberUUID(this.factions, (Player)sender, false);
        Views.openNavigationMenu((Player)sender, f);
      }
      Utils.getPlugin().getLogger().warning(ex.toString());
    }

    return false;
  }
}
