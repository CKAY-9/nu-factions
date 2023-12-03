package dev.ckay9.nu_factions.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Faction;

public class FactionCompletor implements TabCompleter {
  NuFactions factions;

  public FactionCompletor(NuFactions factions) {
    this.factions = factions;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return Collections.emptyList();
    }

    Player p = (Player)sender;
    Faction f = Faction.getFactionFromMemberUUID(this.factions, p, false);

    if (args.length == 1) {
      ArrayList<String> completors = new ArrayList<String>();
      completors.add("create");
      completors.add("join");

      if (f != null) {
        if (f.faction_leader.toString().toLowerCase().equalsIgnoreCase(p.getUniqueId().toString())) {
          completors.add("claim");
          completors.add("delete");
        } else {
          completors.add("leave");
        }
        completors.add("info");
      }

      return completors;
    }

    if (args.length == 2 && args[1].toLowerCase().contains("create")) {
      return Collections.singletonList("name");      
    }

    if (f == null) {
      return Collections.emptyList();
    }

    boolean is_leader = f.faction_leader.toString().equalsIgnoreCase(p.getUniqueId().toString());
    if (args.length == 2 && args[1].contains("claim") && is_leader) {
      ArrayList<String> completors = new ArrayList<String>();
      completors.add("new");
      if (f.faction_claims.size() >= 1) {
        completors.add("delete");
      }
    }

    if (args.length == 3 && args[1].contains("claim") && args[2].contains("new") && is_leader) {
      return Collections.singletonList("name");
    }
    if (args.length == 4 && args[1].contains("claim") && args[2].contains("new") && is_leader) {
      return Collections.singletonList("radius");
    }

    return null;
  }
}
