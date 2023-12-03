package dev.ckay9.nu_factions.Commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Claim;
import dev.ckay9.nu_factions.Factions.Faction;
import dev.ckay9.nu_factions.Utils.Utils;

public class FactionCommand implements CommandExecutor {
  public NuFactions factions;

  public FactionCommand(NuFactions factions) {
    this.factions = factions;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return false;
    }

    try {
      String subcommand = args[0].toLowerCase();
      Player player = (Player) sender;
      Faction f = Faction.getFactionFromMemberUUID(this.factions, player, false);

      if (subcommand.contains("create")) {
        if (f != null) {
          player.sendMessage(Utils.formatText("&cYou are currently in a faction!"));
          return false;
        }
        
        String faction_name = args[1];
        ArrayList<UUID> uuids = new ArrayList<UUID>();
        uuids.add(player.getUniqueId());
        Faction new_faction = new Faction(new ArrayList<Claim>(), uuids, faction_name, player.getUniqueId(), 100);
        new_faction.saveFactionData();
        this.factions.factions.add(new_faction);
        player.sendMessage(Utils.formatText("&aSuccesfully created faction " + faction_name));
        return true; 
      }

      if (subcommand.contains("info")) {
        player.sendMessage(Utils.formatText("&aYour Current Faction: " + f.faction_name));
        if (f != null & f.active_members != null) {
          player.sendMessage(Utils.formatText("&aActive Members: "));
          for (int i = 0; i < f.active_members.size(); i++) {
            player.sendMessage(Utils.formatText("&a - " + f.active_members.get(i).getName()));
          }
        }
        return true;
      }

      if (subcommand.contains("claim")) {
        String type = args[1].toLowerCase();

        if (type.contains("new")) {
          String claim_name = args[2];
          int claim_radius = Integer.parseInt(args[3]);
        }
      }

    } catch (Exception ex) {
      sender.sendMessage(Utils.formatText("&c" + ex.toString()));
      Utils.getPlugin().getLogger().warning(ex.toString());
    }

    return false;
  }
}
