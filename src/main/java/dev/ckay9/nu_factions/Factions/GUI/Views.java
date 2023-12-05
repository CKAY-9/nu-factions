package dev.ckay9.nu_factions.Factions.GUI;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Claim;
import dev.ckay9.nu_factions.Factions.Faction;
import dev.ckay9.nu_factions.Factions.FactionClaim;
import dev.ckay9.nu_factions.Utils.Utils;

public class Views {
  public static void openBoardMenu(Player player, NuFactions factions) {
    player.closeInventory();
    Inventory board_inventory = Bukkit.createInventory(null, 27, Utils.formatText("&c&lNu-Factions: Leaderboard"));
    board_inventory.clear();
    int runnnig_total = 10;
    LinkedHashMap<String, Long> entries = Utils.getLeaderboard(factions);
    int i = 0;
    for (String key : entries.keySet()) {
      if (i >= 5) {
        break;
      }
      Faction faction = Faction.getFactionFromName(factions, key);
      if (faction == null) {
        continue;
      }
      ItemStack board_entry = new ItemStack(Material.RED_CONCRETE);
      ItemMeta board_meta = board_entry.getItemMeta();
      board_meta.setDisplayName(Utils.formatText("&c&l" + (i + 1) + ". " + key + ": " + faction.faction_power));
      board_entry.setItemMeta(board_meta);
      board_inventory.setItem(runnnig_total++, board_entry);
      i++;
    }
    player.openInventory(board_inventory);
  }

  public static void openJoinMenu(Player player, NuFactions factions) {
    player.closeInventory();
    Inventory join_inventory = Bukkit.createInventory(null, 54, Utils.formatText("&c&lNu-Factions: Join Faction"));
    join_inventory.clear();
    ArrayList<Faction> invitations = Faction.getAllFactionInvites(factions, player);
    for (int i = 0; i < invitations.size(); i++) {
      if (i >= 54) {
        break;
      }
      Faction faction = invitations.get(i);
      ItemStack temp_item = new ItemStack(Material.PAPER, 1);
      ItemMeta temp_meta = temp_item.getItemMeta();
      temp_meta.setDisplayName("&aInvitation from " + faction.faction_name);
      temp_item.setItemMeta(temp_meta);
      join_inventory.setItem(i, temp_item);
    }
    player.openInventory(join_inventory);
  }
  
  public static void openInformationMenu(Player player, Faction faction, NuFactions factions) {
    player.closeInventory();
    if (faction == null) {
      return;
    }

    Inventory info_inventory = Bukkit.createInventory(null, 27, Utils.formatText("&c&lNu-Factions: Faction Information"));
    info_inventory.clear();

    ItemStack name_block = new ItemStack(Material.SKELETON_SKULL, 1);
    ItemMeta name_meta = name_block.getItemMeta();
    name_meta.setDisplayName(Utils.formatText("&cFaction: " + faction.faction_name));
    name_block.setItemMeta(name_meta);
    info_inventory.setItem(10, name_block);

    ItemStack active_block = new ItemStack(Material.BELL, 1);
    ItemMeta active_meta = active_block.getItemMeta();
    active_meta.setDisplayName(Utils.formatText("&cActive Members: " + faction.active_members.size()));
    active_block.setItemMeta(active_meta);
    info_inventory.setItem(11, active_block);

    ItemStack power_block = new ItemStack(Material.NETHER_STAR, 1);
    ItemMeta power_meta = power_block.getItemMeta();
    power_meta.setDisplayName(Utils.formatText("&cFaction Power: " + faction.faction_power));
    power_block.setItemMeta(power_meta);
    info_inventory.setItem(12, power_block);

    FactionClaim claim = Claim.getCurrentPlayerClaim(player, factions);
    if (claim != null) {
      ItemStack claim_block = new ItemStack(Material.MAP, 1);
      ItemMeta claim_meta = claim_block.getItemMeta();
      claim_meta.setDisplayName(Utils.formatText("&cCurrent Claim: " + claim.claim.claim_name + ", " + claim.faction.faction_name));
      claim_block.setItemMeta(claim_meta);
      info_inventory.setItem(13, claim_block);
    }
   
    player.openInventory(info_inventory);
  }

  public static void openNavigationMenu(Player player, Faction faction) {
    player.closeInventory();
    Inventory nav_inventory = Bukkit.createInventory(null, 27, Utils.formatText("&c&lNu-Factions: Navigation"));
    nav_inventory.clear();
    int running_total = 10;

    ItemStack board_button = new ItemStack(Material.BLACK_CONCRETE, 1);
    ItemMeta board_meta = board_button.getItemMeta();
    board_meta.setDisplayName(Utils.formatText("&c&lLEADERBOARD"));
    board_button.setItemMeta(board_meta);
    nav_inventory.setItem(running_total++, board_button);


    if (player.isOp()) {
      ItemStack admin_button = new ItemStack(Material.RED_CONCRETE, 1);
      ItemMeta admin_meta = admin_button.getItemMeta();
      admin_meta.setDisplayName(Utils.formatText("&c&lADMIN"));
      admin_button.setItemMeta(admin_meta);
      nav_inventory.setItem(running_total++, admin_button);
    }

    if (faction == null) {
      ItemStack create_button = new ItemStack(Material.GREEN_CONCRETE, 1);
      ItemMeta create_meta = create_button.getItemMeta();
      create_meta.setDisplayName(Utils.formatText("&a&lCREATE"));
      create_button.setItemMeta(create_meta);
      nav_inventory.setItem(running_total++, create_button);
     
      ItemStack join_button = new ItemStack(Material.BLUE_CONCRETE, 1);
      ItemMeta join_meta = join_button.getItemMeta();
      join_meta.setDisplayName(Utils.formatText("&9&lJOIN"));
      join_button.setItemMeta(join_meta);
      nav_inventory.setItem(running_total++, join_button);
    } else {
      if (faction.isPlayerLeader(player)) {
        ItemStack claim_button = new ItemStack(Material.YELLOW_CONCRETE, 1);
        ItemMeta claim_meta = claim_button.getItemMeta();
        claim_meta.setDisplayName(Utils.formatText("&e&lCLAIM"));
        claim_button.setItemMeta(claim_meta);
        nav_inventory.setItem(running_total++, claim_button);

        ItemStack delete_button = new ItemStack(Material.RED_CONCRETE, 1);
        ItemMeta delete_meta = delete_button.getItemMeta();
        delete_meta.setDisplayName(Utils.formatText("&c&lDELETE"));
        delete_button.setItemMeta(delete_meta);
        nav_inventory.setItem(running_total++, delete_button);
        
        ItemStack invite_button = new ItemStack(Material.ORANGE_CONCRETE, 1);
        ItemMeta invite_meta = invite_button.getItemMeta();
        invite_meta.setDisplayName(Utils.formatText("&6&lINVITE"));
        invite_button.setItemMeta(invite_meta);
        nav_inventory.setItem(running_total++, invite_button);
      } else {
        ItemStack leave_button = new ItemStack(Material.PURPLE_CONCRETE, 1);
        ItemMeta leave_meta = leave_button.getItemMeta();
        leave_meta.setDisplayName(Utils.formatText("&6&lINVITE"));
        leave_button.setItemMeta(leave_meta);
        nav_inventory.setItem(running_total++, leave_button);
      }
      ItemStack info_button = new ItemStack(Material.WHITE_CONCRETE, 1);
      ItemMeta info_meta = info_button.getItemMeta();
      info_meta.setDisplayName(Utils.formatText("&lINFO"));
      info_button.setItemMeta(info_meta);
      nav_inventory.setItem(running_total++, info_button);
    }

    player.openInventory(nav_inventory);
  }

}
