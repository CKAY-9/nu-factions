package dev.ckay9.nu_factions.Factions;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Utils.Vector3;

public class Claim {
  public Vector3 starting_positon;
  public Vector3 ending_position;
  public String claim_name;
  public ArrayList<Player> players_inside = new ArrayList<Player>();

  public Claim(Vector3 start, Vector3 end, String name) {
    this.starting_positon = start;
    this.ending_position = end;
    this.claim_name = name;
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
    int side_length = Math.abs(this.starting_positon.x) - Math.abs(this.ending_position.x);
    return side_length * side_length;
  }

  // I love loops
  public static boolean doesClaimCollideWithOthers(Location starting, Location ending, NuFactions nu_factions) {
    for (int fi = 0; fi < nu_factions.factions.size(); fi++) {
      Faction f = nu_factions.factions.get(fi);
      for (int c = 0; c < f.faction_claims.size(); c++) {
        Claim claim = f.faction_claims.get(c);
        for (int x = claim.ending_position.x; x < claim.starting_positon.x; x++) {
          for (int z = claim.ending_position.z; z < claim.starting_positon.z; z++) {
            for (int xx = ending.getBlockX(); xx < starting.getBlockX(); xx++) {
              for (int zz = ending.getBlockZ(); zz < starting.getBlockZ(); zz++) {
                if (x == xx && z == zz) {
                  return true;
                } 
              }
            } 
          }
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
        for (int x = claim.ending_position.x; x < claim.starting_positon.x; x++) {
          for (int z = claim.ending_position.z; z < claim.starting_positon.z; z++) {
            if (player_location.getBlockX() == x && player_location.getBlockZ() == z) {
              return new FactionClaim(f, claim);
            }          
          }
        }
      }
    }
    return null;
  }
}
