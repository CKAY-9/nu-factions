package dev.ckay9.nu_factions.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class FactionCompletor implements TabCompleter {
  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 1) {
      ArrayList<String> completors = new ArrayList<String>();
      completors.add("create");
      completors.add("join");
      return completors;
    }
    return null;
  }
}
