package dev.ckay9.nu_factions.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Faction;
import net.md_5.bungee.api.ChatColor;

public class Utils { 
  public static String formatText(String s) {
    return ChatColor.translateAlternateColorCodes('&', s);
  }

  public static NuFactions getPlugin() {
    return (NuFactions)Bukkit.getPluginManager().getPlugin("NuFactions");
  }

  public static ItemStack generateBackButton() {
    ItemStack back_button = new ItemStack(Material.BARRIER, 1);
    ItemMeta back_meta = back_button.getItemMeta();
    back_meta.setDisplayName(formatText("&c&lCLOSE/BACK"));
    back_button.setItemMeta(back_meta);
    return back_button;
  }

  public static ItemStack generateNextButton() {
    ItemStack next_button = new ItemStack(Material.BOOK, 1);
    ItemMeta next_meta = next_button.getItemMeta();
    next_meta.setDisplayName(formatText("&a&lNEXT"));
    next_button.setItemMeta(next_meta);
    return next_button;
  }

  public static ArrayList<Player> getOnlinePlayers() {
    ArrayList<Player> players = new ArrayList<Player>();
    for (Player ply : Bukkit.getOnlinePlayers()) {
      players.add(ply);
    }
    return players;
  }

  public static LinkedHashMap<String, Long> getLeaderboard(NuFactions factions) {
    HashMap<String, Long> entries = new HashMap<String, Long>();
    LinkedHashMap<String, Long> sorted = new LinkedHashMap<String, Long>();
    ArrayList<Long> list = new ArrayList<Long>();
    for (int i = 0; i < factions.factions.size(); i++) {
      Faction f = factions.factions.get(i);
      entries.put(f.faction_name, f.calculateTotalPowerCost() + f.faction_power);
    }
    for (Map.Entry<String, Long> entry : entries.entrySet()) {
      list.add(entry.getValue());
    }
    Collections.sort(list);
    Collections.reverse(list);
    for (int i = 0; i < list.size(); i++) {
      for (Map.Entry<String, Long> entry : entries.entrySet()) {
        if (entry.getValue().equals(list.get(i))) {
          sorted.put(entry.getKey(), list.get(i));
        }
      }
    }
    return sorted;
  }
}
