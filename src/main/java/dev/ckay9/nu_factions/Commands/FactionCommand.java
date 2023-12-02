package dev.ckay9.nu_factions.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import dev.ckay9.nu_factions.NuFactions;

public class FactionCommand implements CommandExecutor {
  public NuFactions factions;

  public FactionCommand(NuFactions factions) {
    this.factions = factions;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    return false;
  }
}
