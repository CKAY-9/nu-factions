package dev.ckay9.nu_factions.Factions;

import java.util.ArrayList;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import dev.ckay9.nu_factions.Data;
import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Utils.Utils;
import dev.ckay9.nu_factions.Utils.Vector3;

public class Claim {
  public Vector3 starting_positon;
  public Vector3 ending_position;
  public String claim_name;
  public ArrayList<Player> players_inside = new ArrayList<Player>();
  public int radius;

  public Claim(Vector3 start, Vector3 end, String name) {
    this.starting_positon = start;
    this.ending_position = end;
    this.claim_name = name;
    this.radius = this.calculateRadius();
  } 

  public boolean isPlayerInside(Player player) {
    for (int i = 0; i < this.players_inside.size(); i++) {
      Player temp = this.players_inside.get(i);
      if (temp.getUniqueId().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
        return true;
      }
    }
    return false;
  }

  public int calculateArea() {
    return calculateSideLength() * calculateSideLength();
  }

  public int calculateRadius() {
    return (int)Math.round(calculateSideLength() / 2);
  }

  public int calculateSideLength() {
    return Math.abs(this.starting_positon.x - this.ending_position.x);
  }

  public Vector3 getCenterCoordinates() {
    return new Vector3(this.starting_positon.x - radius, 0, this.starting_positon.z - radius);
  }

  public static Vector3 convertLocationsToCenterCoordinates(Location starting, Location ending) {
    int radius = Math.abs(starting.getBlockX() - ending.getBlockX()) / 2;
    return new Vector3(starting.getBlockX() - radius, 0, starting.getBlockZ() - radius);
  }

  public void changeRadius(Player player, Faction faction, NuFactions factions, int new_radius) {
    int cost = Claim.getCost(new_radius * 2);
    if (cost > faction.faction_power) {
      player.sendMessage(Utils.formatText("&cYour faction doesn't have enough power to do this change!"));
      return;
    }

    int prev_radius = (int)Math.round(this.calculateSideLength() / 2);
    Location starting = new Location(
      player.getWorld(), 
      this.starting_positon.x - prev_radius, 
      0, 
      this.starting_positon.z - prev_radius
    ).add(new_radius, 320, new_radius);
    Location ending = new Location(
      player.getWorld(), 
      this.starting_positon.x - prev_radius, 
      0, 
      this.starting_positon.z - prev_radius
    ).add(-new_radius, -64, -new_radius);

    boolean does_collide = Claim.doesClaimCollideWithOthers(starting, ending, factions, this);
    if (does_collide) {
      player.sendMessage(Utils.formatText("&cThis change will run into other claims!"));
      return;
    }

    this.starting_positon = new Vector3(starting.getBlockX(), 320, starting.getBlockZ());
    this.ending_position = new Vector3(ending.getBlockX(), -64, ending.getBlockZ());
    faction.saveFactionData();
    player.sendMessage(Utils.formatText("&aSuccessfully updated claim!"));
  }

  public static int getCost(int side_length) {
    return (int)Math.floor(side_length * Data.config_data.getDouble("config.claim_cost_multiplier", 0.2));
  }

  public static Claim getClaimFromName(Faction faction, String name) {
    for (int i = 0; i < faction.faction_claims.size(); i++) {
      Claim claim = faction.faction_claims.get(i);
      if (claim.claim_name.equals(name)) {
        return claim;
      }
    } 
    return null;
  }

  public static boolean doesClaimCollideWithOthers(Location starting, Location ending, NuFactions nu_factions, @Nullable Claim to_ignore) {
    for (int fi = 0; fi < nu_factions.factions.size(); fi++) {
      Faction f = nu_factions.factions.get(fi);
      for (int fc = 0; fc < f.faction_claims.size(); fc++) {
        Claim claim = f.faction_claims.get(fc);
        if (claim == to_ignore) {
          continue;
        }
        Vector3 center_coords = claim.getCenterCoordinates();
        Vector3 new_center_coords = convertLocationsToCenterCoordinates(starting, ending);
        double distance = Math.hypot(
          (new_center_coords.x - center_coords.x),
          (new_center_coords.z - center_coords.z)
        );
        // compensate since radius around a square aint a single radius
        double radius = Math.hypot(
          Math.min(Math.abs(center_coords.x - new_center_coords.x), claim.radius), 
          Math.min(Math.abs(center_coords.z - new_center_coords.z), claim.radius)
        );
        if (distance <= radius) {
          return true;
        }
      }
    }
    return false;
  }

  public static FactionClaim getCurrentPlayerClaim(Player player, NuFactions nu_factions) {
    for (int i = 0; i < nu_factions.factions.size(); i++) {
      Faction faction = nu_factions.factions.get(i);
      for (int k = 0; k < faction.faction_claims.size(); k++) {
        Claim claim = faction.faction_claims.get(k);
        if (claim.isPlayerInside(player)) {
          return new FactionClaim(faction, claim);
        }
      }
    }
    return null;
  }

  
  public static FactionClaim getCurrentClaim(Location player_location, NuFactions nu_factions) {
    for (int fi = 0; fi < nu_factions.factions.size(); fi++) {
      Faction f = nu_factions.factions.get(fi);
      for (int c = 0; c < f.faction_claims.size(); c++) {
        Claim claim = f.faction_claims.get(c);
        Vector3 center_coords = claim.getCenterCoordinates();
        double distance = Math.hypot(
          (player_location.getBlockX() - center_coords.x),
          (player_location.getBlockZ() - center_coords.z)
        );
        // compensate since radius around a square aint a single radius
        double radius = Math.hypot(
          Math.min(Math.abs(center_coords.x - player_location.getBlockX()), claim.radius), 
          Math.min(Math.abs(center_coords.z - player_location.getBlockZ()), claim.radius)
        );
        if (distance <= radius) {
          return new FactionClaim(f, claim);
        }     
      }
    }
    return null;
  }
}
