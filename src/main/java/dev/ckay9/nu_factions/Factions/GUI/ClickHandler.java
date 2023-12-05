package dev.ckay9.nu_factions.Factions.GUI;

import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Faction;

import java.util.ArrayList;
import java.util.HashMap;

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
        case "create":
          break;
        case "join":
          Views.openJoinMenu(player, this.factions);
          break;
        case "claim":
          break;
        case "delete":
          break;
        case "invite":
          break;
        case "leave":
          Views.openBoardMenu(player, this.factions);
          break;
        case "info":
          break;
      }
    }
  }

  private void handleJoin(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
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

    String inv_title = event.getView().getTitle();
    if (inv_title.contains("Navigation")) {
      handleNavigation(event);
      return;
    }
    if (inv_title.contains("Join")) {
      handleJoin(event);
      return;
    }
  }
}
