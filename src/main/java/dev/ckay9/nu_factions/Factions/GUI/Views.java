package dev.ckay9.nu_factions.Factions.GUI;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Claim;
import dev.ckay9.nu_factions.Factions.Faction;
import dev.ckay9.nu_factions.Factions.FactionClaim;
import dev.ckay9.nu_factions.Utils.Utils;

public class Views {
  private static void generateSliderInventory(Inventory inv, int lowest, int middle, int highest, long change) {
    // Decrease
    ItemStack decrease_five = new ItemStack(Material.RED_CONCRETE, 1);
    ItemMeta df_meta = decrease_five.getItemMeta();
    df_meta.setDisplayName(Utils.formatText("&c&l-" + lowest));
    decrease_five.setItemMeta(df_meta);
    inv.setItem(12, decrease_five);

    ItemStack decrease_ten = new ItemStack(Material.RED_WOOL, 1);
    ItemMeta dt_meta = decrease_ten.getItemMeta();
    dt_meta.setDisplayName(Utils.formatText("&c&l-" + middle));
    decrease_ten.setItemMeta(dt_meta);
    inv.setItem(11, decrease_ten);

    ItemStack decrease_twenty_five = new ItemStack(Material.RED_STAINED_GLASS, 1);
    ItemMeta dtf_meta = decrease_twenty_five.getItemMeta();
    dtf_meta.setDisplayName(Utils.formatText("&c&l-" + highest));
    decrease_twenty_five.setItemMeta(dtf_meta);
    inv.setItem(10, decrease_twenty_five);

    // Information
    ItemStack info = new ItemStack(Material.WHITE_CONCRETE, 1);
    ItemMeta info_meta = info.getItemMeta();
    info_meta.setDisplayName(Utils.formatText("&lCONFIRM CHANGE: " + change));
    info.setItemMeta(info_meta);
    inv.setItem(13, info);

    // Increase 
    ItemStack increase_five = new ItemStack(Material.GREEN_STAINED_GLASS, 1);
    ItemMeta if_meta = increase_five.getItemMeta();
    if_meta.setDisplayName(Utils.formatText("&a&l+" + lowest));
    increase_five.setItemMeta(if_meta);
    inv.setItem(14, increase_five);

    ItemStack increase_ten = new ItemStack(Material.GREEN_WOOL, 1);
    ItemMeta it_meta = increase_ten.getItemMeta();
    it_meta.setDisplayName(Utils.formatText("&a&l+" + middle));
    increase_ten.setItemMeta(it_meta);
    inv.setItem(15, increase_ten);

    ItemStack increase_twenty_five = new ItemStack(Material.GREEN_CONCRETE, 1);
    ItemMeta itf_meta = increase_twenty_five.getItemMeta();
    itf_meta.setDisplayName(Utils.formatText("&a&l+" + highest));
    increase_twenty_five.setItemMeta(itf_meta);
    inv.setItem(16, increase_twenty_five);
  }

  public static void openBoardMenu(Player player, NuFactions factions) {
    player.closeInventory();
    Inventory board_inventory = Bukkit.createInventory(null, 27, Utils.formatText("&c&lNu-Factions: Leaderboard"));
    board_inventory.clear();

    board_inventory.setItem(ClickTypes.BACK_CLOSE_SMALL_MENU, Utils.generateBackButton());

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

    join_inventory.setItem(ClickTypes.BACK_CLOSE_LARGE_MENU, Utils.generateBackButton());

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

  public static void openClaimChoiceMenu(Player player, Faction faction, NuFactions factions) {
    player.closeInventory();
    if (faction == null || !faction.isPlayerLeader(player)) {
      return;
    }
    Inventory claim_inventory = Bukkit.createInventory(null, 54, Utils.formatText("&c&lNu-Factions: Claims"));
    claim_inventory.clear();

    claim_inventory.setItem(ClickTypes.BACK_CLOSE_LARGE_MENU, Utils.generateBackButton());
    
    ItemStack new_claim = new ItemStack(Material.MAP, 1);
    ItemMeta new_claim_meta = new_claim.getItemMeta();
    new_claim_meta.setDisplayName(Utils.formatText("&a&lNEW CLAIM"));
    new_claim.setItemMeta(new_claim_meta);
    claim_inventory.setItem(49, new_claim);

    int inv_index = 0;
    for (int i = 0; i < faction.faction_claims.size(); i++) {
      if (inv_index > 54) {
        break;
      }

      Claim claim = faction.faction_claims.get(i);
      ItemStack claim_item = new ItemStack(Material.NETHER_STAR, 1);
      ItemMeta claim_meta = claim_item.getItemMeta();
      claim_meta.setDisplayName(Utils.formatText("&a" + claim.claim_name + ": " + claim.calculateSideLength() + "x" + claim.calculateSideLength()));
      claim_item.setItemMeta(claim_meta);
      claim_inventory.setItem(inv_index, claim_item);
      inv_index++;
    }

    player.openInventory(claim_inventory);
  }

  public static void openSpecificClaimMenu(Player player, Faction faction, Claim claim, NuFactions factions) {
    player.closeInventory();
    if (faction == null || !faction.isPlayerLeader(player) || claim == null) {
      return;
    }
    
    Inventory claim_inventory = Bukkit.createInventory(null, 27, Utils.formatText("&c&lNu-Factions: Claim " + claim.claim_name));
    claim_inventory.setItem(ClickTypes.BACK_CLOSE_SMALL_MENU, Utils.generateBackButton());
    generateSliderInventory(claim_inventory, 5, 10, 25, claim.radius);

    player.openInventory(claim_inventory);
  }
  
  public static void openInformationMenu(Player player, Faction faction, NuFactions factions) {
    player.closeInventory();
    if (faction == null) {
      return;
    }

    Inventory info_inventory = Bukkit.createInventory(null, 27, Utils.formatText("&c&lNu-Factions: Information"));
    info_inventory.clear();

    info_inventory.setItem(ClickTypes.BACK_CLOSE_SMALL_MENU, Utils.generateBackButton());

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

  public static void openInviteMenu(Player player, Faction faction) {
    player.closeInventory();
    if (faction == null || !faction.isPlayerLeader(player)) {
      return;
    }
    Inventory invite_inventory = Bukkit.createInventory(null, 54, Utils.formatText("&c&lNu-Factions: Invite Player"));
    invite_inventory.clear();

    invite_inventory.setItem(ClickTypes.BACK_CLOSE_LARGE_MENU, Utils.generateBackButton());
    if (Bukkit.getOnlinePlayers().size() > ClickTypes.BACK_CLOSE_LARGE_MENU) {
      invite_inventory.setItem(53, Utils.generateNextButton());
    }
 
    ArrayList<Player> players = Utils.getOnlinePlayers();
    int index_to_start = 0;
    if (index_to_start > players.size()) {
      return;
    }
    int inv_index = 0;
    for (int i = index_to_start; i < players.size(); i++) {
      Player ply = players.get(i); 
      ItemStack player_head = new ItemStack(Material.PLAYER_HEAD, 1);
      SkullMeta head_meta = (SkullMeta)player_head.getItemMeta();
      head_meta.setDisplayName(ply.getName());
      head_meta.setOwningPlayer(Bukkit.getOfflinePlayer(ply.getName()));
      player_head.setItemMeta(head_meta);
      invite_inventory.setItem(inv_index, player_head);
      inv_index++;
    } 

    player.openInventory(invite_inventory);
  }

  public static void openAdminMenu(Player player) {
    player.closeInventory();
    if (!player.isOp()) {
      return;
    }

    Inventory admin_inventory = Bukkit.createInventory(null, 27, Utils.formatText("&c&lNu-Factions: Admin"));
    admin_inventory.clear();
    admin_inventory.setItem(ClickTypes.BACK_CLOSE_SMALL_MENU, Utils.generateBackButton());

    ItemStack change_power = new ItemStack(Material.GREEN_CONCRETE, 1);
    ItemMeta change_meta = change_power.getItemMeta();
    change_meta.setDisplayName(Utils.formatText("&a&lADD POWER"));
    change_power.setItemMeta(change_meta);
    admin_inventory.setItem(10, change_power);

    ItemStack set_power = new ItemStack(Material.WHITE_CONCRETE, 1);
    ItemMeta set_meta = set_power.getItemMeta();
    set_meta.setDisplayName(Utils.formatText("&lSET POWER"));
    set_power.setItemMeta(set_meta);
    admin_inventory.setItem(11, set_power);

    ItemStack delete_faction = new ItemStack(Material.BARRIER, 1);
    ItemMeta delete_meta = delete_faction.getItemMeta();
    delete_meta.setDisplayName(Utils.formatText("&c&lDELETE FACTION"));
    delete_faction.setItemMeta(delete_meta);
    admin_inventory.setItem(12, delete_faction);

    player.openInventory(admin_inventory);
  }

  public static void openFactionSelectMenu(Player player, NuFactions factions, AdminView view) {
    player.closeInventory();
    if (!player.isOp()) {
      return;
    }

    Inventory factions_inventory = Bukkit.createInventory(null, 54, Utils.formatText("&c&lNu-Factions: Choose a Faction"));
    factions_inventory.clear();
    factions_inventory.setItem(ClickTypes.BACK_CLOSE_LARGE_MENU, Utils.generateBackButton());
  
    // TODO: get page number and proper factions
    int inv_index = 0;
    for (int i = 0; i < factions.factions.size(); i++) {
      if (inv_index >= ClickTypes.BACK_CLOSE_LARGE_MENU) {
        break;
      }

      Faction faction = factions.factions.get(i);

      ItemStack faction_stack = new ItemStack(Material.NETHERITE_SWORD, 1);
      ItemMeta faction_meta = faction_stack.getItemMeta();
      faction_meta.setDisplayName(Utils.formatText("&a&l" + faction.faction_name));
      faction_stack.setItemMeta(faction_meta);
      factions_inventory.setItem(inv_index, faction_stack);

      inv_index++;
    }

    player.openInventory(factions_inventory);
  }

  public static void openChangePowerMenu(Player player, Faction faction) {
    player.closeInventory();
    if (!player.isOp()) {
      return;
    }

    Inventory power_inventory = Bukkit.createInventory(null, 27, Utils.formatText("&c&lNu-Factions: Change " + faction.faction_name));
    power_inventory.clear();
    power_inventory.setItem(ClickTypes.BACK_CLOSE_SMALL_MENU, Utils.generateBackButton());

    generateSliderInventory(power_inventory, 10, 25, 50, faction.faction_power);

    player.openInventory(power_inventory);
  }

  public static void openNavigationMenu(Player player, Faction faction) {
    player.closeInventory();
    Inventory nav_inventory = Bukkit.createInventory(null, 27, Utils.formatText("&c&lNu-Factions: Navigation"));
    nav_inventory.clear();
    int running_total = 10;

    nav_inventory.setItem(ClickTypes.BACK_CLOSE_SMALL_MENU, Utils.generateBackButton());

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
