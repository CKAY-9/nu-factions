package dev.ckay9.nu_factions.Factions.GUI;

import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import dev.ckay9.nu_factions.Data;
import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Claim;
import dev.ckay9.nu_factions.Factions.Faction;
import dev.ckay9.nu_factions.Utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;


public class ClickHandler implements Listener {
  NuFactions factions;

  public ClickHandler(NuFactions factions) {
    this.factions = factions;
  }

  private void handleNavigation(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    if (event.getSlot() == ClickTypes.BACK_CLOSE_SMALL_MENU) {
      player.closeInventory();
      return;
    }

    HashMap<Integer, String> nav_items = new HashMap<Integer, String>();
    int running_total = 10;
    nav_items.put(running_total++, "board");

    if (event.getWhoClicked().isOp()) {
       nav_items.put(running_total++, "admin");
    }

    Faction faction = Faction.getFactionFromMemberUUID(this.factions, player, false);

    if (faction == null) {
      nav_items.put(running_total++, "create");
      nav_items.put(running_total++, "join");
    } else {
      if (faction.isPlayerLeader(player)) {
        nav_items.put(running_total++, "claim");
        nav_items.put(running_total++, "delete");
        nav_items.put(running_total++, "invite");
      } else {
        nav_items.put(running_total++, "leave");
      }
    }
    nav_items.put(running_total++, "info");

    for (Integer key : nav_items.keySet()) {
      if (key != event.getSlot()) {
        continue;
      }
      switch (nav_items.get(key).toLowerCase()) {
        case "board":
          Views.openBoardMenu(player, this.factions);
          break;
        case "create":
          player.closeInventory();
          player.sendMessage(Utils.formatText("&aYou can create a new clan using the command: /nufactions create NAME"));
          break;
        case "join":
          Views.openJoinMenu(player, this.factions);
          break;
        case "admin":
          Views.openAdminMenu(player);
          break;
        case "claim":
          Views.openClaimChoiceMenu(player, faction, factions);
          break;
        case "delete":
          faction.delete(factions);
          player.closeInventory(); 
          break;
        case "invite":
          Views.openInviteMenu(player, faction);
          break;
        case "leave":
          player.closeInventory();
          faction.leave(player);
          break;
        case "info":
          Views.openInformationMenu(player, faction, factions);
          break;
      }
    }
  }

  private void handleJoin(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();

    if (event.getSlot() == ClickTypes.BACK_CLOSE_LARGE_MENU) {
      Views.openNavigationMenu(player, Faction.getFactionFromMemberUUID(this.factions, player, false));
      return;
    }

    ArrayList<Faction> invitations = Faction.getAllFactionInvites(this.factions, player);
    for (int i = 0; i < invitations.size(); i++) {
      if (i >= 54) {
        break;
      }
      if (event.getSlot() != i) {
        continue;
      }
      Faction faction = invitations.get(i);
      faction.acceptInvite(player);
      player.closeInventory();;
      break;
    } 
  }
  
  private void handleLeaderboard(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();

    if (event.getSlot() == ClickTypes.BACK_CLOSE_SMALL_MENU) {
      Views.openNavigationMenu(player, Faction.getFactionFromMemberUUID(this.factions, player, false));
      return;
    }
  }

  private void handleInfo(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();

    if (event.getSlot() == ClickTypes.BACK_CLOSE_SMALL_MENU) {
      Views.openNavigationMenu(player, Faction.getFactionFromMemberUUID(this.factions, player, false));
      return;
    }
  }

  private void handleInvite(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    Faction faction = Faction.getFactionFromMemberUUID(this.factions, player, false);
    if (faction == null || !faction.isPlayerLeader(player)) {
      player.closeInventory();
      return;
    }

    int slot = event.getSlot();
    if (slot == ClickTypes.BACK_CLOSE_LARGE_MENU) {
      if (Bukkit.getOnlinePlayers().size() > ClickTypes.BACK_CLOSE_LARGE_MENU) {
        Views.openInviteMenu(player, faction);
        return;
      } 
      Views.openNavigationMenu(player, faction);
      return;
    }

    ArrayList<Player> players = Utils.getOnlinePlayers();
    // TODO: get proper player and page
    Player target = players.get(slot);
    if (target == null) {
      return;
    }

    faction.invitePlayer(target, player);
  }

  private void handleClaims(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    Faction faction = Faction.getFactionFromMemberUUID(this.factions, player, false);
    if (faction == null || !faction.isPlayerLeader(player)) {
      player.closeInventory();
      return;
    }

    if (event.getSlot() == ClickTypes.BACK_CLOSE_LARGE_MENU) {
      Views.openNavigationMenu(player, faction);
    }

    if (event.getSlot() == 49) {
      player.closeInventory();
      player.sendMessage(Utils.formatText("&aYou can create new claims using the command: /nufactions claim new NAME RADIUS"));
      return;
    }

    // TODO: Get page number and get proper claim
    int slot = event.getSlot();
    if (slot > faction.faction_claims.size()) {
      return;
    }
    Claim claim = faction.faction_claims.get(slot);
    if (claim == null) {
      return;
    }
    Views.openSpecificClaimMenu(player, faction, claim, this.factions);
  }

  private void handleClaim(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    Faction faction = Faction.getFactionFromMemberUUID(this.factions, player, false);
    if (event.getSlot() == ClickTypes.BACK_CLOSE_SMALL_MENU) {
      Views.openClaimChoiceMenu(player, faction, this.factions);
      return;
    }

    if (faction == null || !faction.isPlayerLeader(player)) {
      return;
    }

    String inv_title = event.getView().getTitle();
    // example title: Nu-Factions:_Claim_NAME
    String claim_name = inv_title.split(" ")[2];
    Claim claim = Claim.getClaimFromName(faction, claim_name);
    if (claim == null) {
      return;
    }

    int slot = event.getSlot();
    switch (slot) {
      case 10:
        claim.radius -= 25;
        break;
      case 11:
        claim.radius -= 10;
        break;
      case 12:
        claim.radius -= 5;
        break;
      case 13:
        claim.changeRadius(player, faction, factions, claim.radius);
        player.closeInventory();
        break;
      case 14:
        claim.radius += 5;
        break;
      case 15:
        claim.radius += 10;
        break;
      case 16:
        claim.radius += 25;
        break;
    }
    Views.openSpecificClaimMenu(player, faction, claim, this.factions);
  }

  private void handleAdmin(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    if (!player.isOp()) {
      return;
    }
    Faction faction = Faction.getFactionFromMemberUUID(this.factions, player, false);
    
    int slot = event.getSlot();
    if (slot == ClickTypes.BACK_CLOSE_SMALL_MENU) {
      Views.openNavigationMenu(player, faction);
      return;
    }

    switch (slot) {
      case 10:
        Views.openFactionSelectMenu(player, this.factions, AdminView.CHANGE_POWER); 
        break;
      case 11:
        player.closeInventory();
        player.sendMessage(Utils.formatText("&aYou can set a faction's power level by using the command: /nufactions admin set-power FACTION VALUE")); 
        break;
      case 12:
        Views.openFactionSelectMenu(player, this.factions, AdminView.DELETE_FACTION);
        break;
    }    
  }

  private void handleFactionPick(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    if (!player.isOp()) {
      return;
    }

    int slot = event.getSlot();
    if (slot == ClickTypes.BACK_CLOSE_LARGE_MENU) {
      Faction faction = Faction.getFactionFromMemberUUID(this.factions, player, false);
      Views.openAdminMenu(player); 
      return;
    }

    // TODO: get proper faction and page
    Faction faction = this.factions.factions.get(slot);
    if (faction == null) {
      return;
    }
    Views.openChangePowerMenu(player, faction);
  }

  private void handlePower(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    if (!player.isOp()) {
      return;
    }

    // i.e Nu-Factions: Change FACTION
    String faction_name = event.getView().getTitle().split(" ")[2];
    Faction faction = Faction.getFactionFromName(this.factions, faction_name);
    if (faction == null) {
      return;
    }

    int slot = event.getSlot();
    if (slot == ClickTypes.BACK_CLOSE_SMALL_MENU) {
      faction.faction_power = Data.factions_data.getLong(faction.faction_leader.toString() + ".power", 100);
      Views.openFactionSelectMenu(player, this.factions, AdminView.CHANGE_POWER);
      return;
    }

    switch (slot) {
      case 10:
        faction.faction_power -= 50;
        break;
      case 11:
        faction.faction_power -= 25;
        break;
      case 12:
        faction.faction_power -= 10;
        break;
      case 13:
        faction.saveFactionData();
        player.closeInventory();
        player.sendMessage(Utils.formatText("&aSuccessfully updated faction power!"));
        break;
      case 14:
        faction.faction_power += 10;
        break;
      case 15:
        faction.faction_power += 25;
        break;
      case 16:
        faction.faction_power += 50;
        break;
    }
    Views.openChangePowerMenu(player, faction);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onGUIClick(InventoryClickEvent event) {
    if (event.getClickedInventory() == null) {
      return;
    }
    if (event.getClickedInventory().getHolder() != null) {
      return;
    }
    if (event.getClickedInventory().getType() != InventoryType.CHEST) {
      return;
    }

    event.setCancelled(true);

    // TODO: not this
    String inv_title = event.getView().getTitle();
    if (inv_title.contains("Navigation")) {
      handleNavigation(event);
      return;
    }
    if (inv_title.contains("Join")) {
      handleJoin(event);
      return;
    }
    if (inv_title.contains("Leaderboard")) {
      handleLeaderboard(event);
      return;
    }
    if (inv_title.contains("Information")) {
      handleInfo(event);
      return;
    }
    if (inv_title.contains("Invite")) {
      handleInvite(event);
      return;
    }
    if (inv_title.contains("Admin")) {
      handleAdmin(event);
      return;
    }
    if (inv_title.contains(" Faction")) {
      handleFactionPick(event);
      return;
    }
    if (inv_title.contains("Change")) {
      handlePower(event);
      return;
    }
    if (inv_title.contains("Claims")) {
      handleClaims(event);
      return;
    }
    if (inv_title.contains("Claim")) {
      handleClaim(event);
      return;
    }
  }
}
