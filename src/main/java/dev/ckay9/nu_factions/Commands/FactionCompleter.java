package dev.ckay9.nu_factions.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Claim;
import dev.ckay9.nu_factions.Factions.Faction;

public class FactionCompleter implements TabCompleter {
  NuFactions factions;

  public FactionCompleter(NuFactions factions) {
    this.factions = factions;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return Collections.emptyList();
    }

    Player player = (Player)sender;
    Faction faction = Faction.getFactionFromMemberUUID(this.factions, player, false);

    if (args.length == 1) {
      ArrayList<String> completors = new ArrayList<String>();
      completors.add("leaderboard");
      completors.add("gui");

      if (player.isOp()) {
        completors.add("admin");
      }

      if (faction == null) {
        completors.add("create");
        completors.add("join");
      }

      if (faction != null) {
        if (faction.isPlayerLeader(player)) {
          completors.add("claim");
          completors.add("delete");
          completors.add("invite");
        } else {
          completors.add("leave");
        }
        completors.add("info");
      }

      return completors;
    }

    if (args.length == 2) {
      if (args[0].toLowerCase().contains("create")) {
        return Collections.singletonList("name");      
      }

      if (player.isOp()) {
        if (args[0].toLowerCase().contains("admin")) {
          ArrayList<String> completors = new ArrayList<String>();
          completors.add("give-power");
          completors.add("take-power");
          completors.add("set-power");
          completors.add("delete-faction");
          return completors;
        }
      }

      if (args[0].toLowerCase().contains("join")) {
        ArrayList<String> completors = new ArrayList<String>();
        ArrayList<Faction> factions = Faction.getAllFactionInvites(this.factions, player);
        for (int i = 0; i < factions.size(); i++) {
          Faction f = factions.get(i); 
          completors.add(f.faction_name);
        }
        return completors;
      }

      if (faction != null && faction.isPlayerLeader(player)) {
        if (args[0].contains("claim")) {
          ArrayList<String> completors = new ArrayList<String>();
          completors.add("new");
          if (faction.faction_claims.size() >= 1) {
            completors.add("delete");
            completors.add("change");
          }
          return completors;
        }
      }
    }

    if (args.length == 3) {
      if (player.isOp()) {
        if (args[0].toLowerCase().contains("admin")) {
          ArrayList<String> completors = new ArrayList<String>();
          for (int i = 0; i < this.factions.factions.size(); i++) {
            completors.add(this.factions.factions.get(i).faction_name);
          }
          return completors;
        }
      }

      if (faction != null & faction.isPlayerLeader(player)) {
        if (args[0].contains("claim") && (args[1].contains("delete") || args[1].contains("change")) && faction.isPlayerLeader(player)) {
          ArrayList<String> completors = new ArrayList<String>();
          for (int i = 0; i < faction.faction_claims.size(); i++) {
            Claim claim = faction.faction_claims.get(i);
            completors.add(claim.claim_name);
          }
          return completors;
        }
        return Collections.singletonList("name");
      }
    }

    if (args.length == 4) {
      if (faction != null && faction.isPlayerLeader(player)) {
        if (args[0].contains("claim")) {
          if (args[1].contains("new")) {
            return Collections.singletonList("radius");
          }
          if (args[1].contains("change")) {
            return Collections.singletonList("new_radius");
          }
        }
      }
      if (player.isOp()) {
        return Collections.singletonList("new_value");
      }
    }

    return null;
  }
}
