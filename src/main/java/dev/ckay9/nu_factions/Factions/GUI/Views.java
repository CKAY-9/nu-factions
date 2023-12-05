package dev.ckay9.nu_factions.Factions.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.ckay9.nu_factions.Factions.Faction;
import dev.ckay9.nu_factions.Utils.Utils;

public class Views {
  public static Inventory generateNavigationInventory(Player player, Faction faction) {
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

    return nav_inventory;
  }

}
