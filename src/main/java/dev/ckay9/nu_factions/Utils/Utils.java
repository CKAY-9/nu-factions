package dev.ckay9.nu_factions.Utils;

import org.bukkit.Bukkit;

import dev.ckay9.nu_factions.NuFactions;
import net.md_5.bungee.api.ChatColor;

public class Utils { 
  public static String formatText(String s) {
    return ChatColor.translateAlternateColorCodes('&', s);
  }
  public static NuFactions getPlugin() {
    return (NuFactions)Bukkit.getPluginManager().getPlugin("NuFactions");
  }
}
