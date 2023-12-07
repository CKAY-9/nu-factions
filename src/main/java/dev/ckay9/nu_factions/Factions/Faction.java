package dev.ckay9.nu_factions.Factions;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import dev.ckay9.nu_factions.Data;
import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Utils.Utils;
import dev.ckay9.nu_factions.Utils.Vector3;

public class Faction {
  public ArrayList<Claim> faction_claims = new ArrayList<Claim>();
  public long faction_power = 100;
  public String faction_name;
  public ArrayList<UUID> faction_members = new ArrayList<UUID>();
  public UUID faction_leader;
  public ArrayList<Player> active_members = new ArrayList<Player>();
  public ArrayList<Player> invites = new ArrayList<Player>();

  public Faction(ArrayList<Claim> claims, ArrayList<UUID> members, String name, UUID leader, long faction_power) {
    this.faction_members = members;
    this.faction_name = name;
    this.faction_claims = claims;
    this.faction_leader = leader;
    this.faction_power = faction_power;

    for (Player p : Bukkit.getOnlinePlayers()) {
      for (int i = 0; i < this.faction_members.size(); i++) {
        if (p.getUniqueId() == this.faction_members.get(i)) {
          this.active_members.add(p);
        }
      }
    }
  }

  public void acceptInvite(Player player) {
    if (!this.invites.contains(player)) {
      player.sendMessage(Utils.formatText("&cYou have not been invited to this faction!"));
      return;
    }

    this.invites.remove(player);
    player.sendMessage(Utils.formatText("&aYou have joined " + this.faction_name));
    this.faction_members.add(player.getUniqueId());
    this.active_members.add(player);
    this.saveFactionData();
  }

  public boolean isPlayerLeader(Player player) {
    return (player.getUniqueId().toString().equalsIgnoreCase(this.faction_leader.toString()));
  }

  public void leave(Player player) {
    if (!this.faction_members.contains(player.getUniqueId())) {
      return;
    }

    this.active_members.remove(player);
    this.faction_members.remove(player.getUniqueId());
    this.saveFactionData();
    player.sendMessage(Utils.formatText("&aYou have left your faction!"));
  }

  public void invitePlayer(Player target, Player self) {
    if (!this.isPlayerLeader(self)) {
      self.sendMessage(Utils.formatText("&cYou must be a faction leader to execute this command!"));
      return;
    }

    this.invites.add(target); 
    self.sendMessage(Utils.formatText("&aYou have invited " + target.getName() + " to your faction!"));
    target.sendMessage(Utils.formatText("&aYou have been invited to join " + this.faction_name + "! Do /nufactions join " + this.faction_name));
  }

  public void saveFactionData() {
    String id = this.faction_leader.toString();
    Data.factions_data.set(id + ".name", this.faction_name);
    Data.factions_data.set(id + ".power", this.faction_power);
    ArrayList<String> uuids_to_strings = new ArrayList<String>();
    for (int k = 0; k < this.faction_members.size(); k++) {
      uuids_to_strings.add(this.faction_members.get(k).toString());
    }
    Data.factions_data.set(id + ".members", uuids_to_strings);
    Data.factions_data.set(id + ".leader", this.faction_leader.toString());
    for (int i = 0; i < this.faction_claims.size(); i++) {
      Claim current_claim = this.faction_claims.get(i);
      Data.factions_data.set(id + ".claims." + current_claim.claim_name + ".name", current_claim.claim_name);
      Data.factions_data.set(id + ".claims." + current_claim.claim_name + ".start.x", current_claim.starting_positon.x);
      Data.factions_data.set(id + ".claims." + current_claim.claim_name + ".start.y", current_claim.starting_positon.y);
      Data.factions_data.set(id + ".claims." + current_claim.claim_name + ".start.z", current_claim.starting_positon.z);
      Data.factions_data.set(id + ".claims." + current_claim.claim_name + ".end.x", current_claim.ending_position.x);
      Data.factions_data.set(id + ".claims." + current_claim.claim_name + ".end.y", current_claim.ending_position.y);
      Data.factions_data.set(id + ".claims." + current_claim.claim_name + ".end.z", current_claim.ending_position.z);
    } 
    try {
      Data.factions_data.save(Data.factions_file);
    } catch (IOException ex) {
      Utils.getPlugin().getLogger().warning(ex.toString());
    }
  }

  public long calculateTotalPowerCost() {
    long total = 0;
    for (int i = 0; i < this.faction_claims.size(); i++) {
      Claim claim = this.faction_claims.get(i);
      total += Claim.getCost(claim.calculateSideLength());
    }
    return total;
  }

  public long calculateTotalLandArea() {
    long total = 0;
    for (int i = 0; i < this.faction_claims.size(); i++) {
      Claim claim = this.faction_claims.get(i);
      total += claim.calculateArea();
    }
    return total;
  }

  public void delete(NuFactions factions) {
    this.active_members.clear();
    this.faction_members.clear();
    this.faction_claims.clear();
    Data.factions_data.set(this.faction_leader.toString(), null);
    try {
      Data.factions_data.save(Data.factions_file);
    } catch (IOException ex) {
      Utils.getPlugin().getLogger().warning(ex.toString());
    }
    factions.factions.remove(this);
  }

  public void deletePowerlessClaims() {
    for (int i = 0; i < this.faction_claims.size(); i++) {
      Claim claim = this.faction_claims.get(i);
      long cost = (long)this.calculateTotalPowerCost();
      if (cost > this.faction_power) {
        for (int p = 0; p < this.active_members.size(); p++) {
          Player player = this.active_members.get(p);
          player.sendMessage(Utils.formatText("&cYou don't have enough power to maintain " + claim.claim_name + "! It is being removed from your claims."));
        }
        Data.factions_data.set(faction_leader.toString() + ".claims." + claim.claim_name, null);
        this.faction_claims.remove(claim);
        continue;
      }
      this.faction_power -= cost;
    }
    this.saveFactionData();
  }

  public static Faction getFactionFromName(NuFactions nu_factions, String name) {
    for (int i = 0; i < nu_factions.factions.size(); i++) {
      Faction faction = nu_factions.factions.get(i);
      if (faction.faction_name.equals(name)) {
        return faction;
      }
    }
    return null;
  } 

  public static ArrayList<Faction> getAllFactionInvites(NuFactions nu_factions, Player player) {
    ArrayList<Faction> factions = new ArrayList<Faction>();
    for (int i = 0; i < nu_factions.factions.size(); i++) {
      Faction faction = nu_factions.factions.get(i);
      if (faction.invites.contains(player)) {
        factions.add(faction);
      }
    }
    return factions;
  }

  public static Faction getFactionFromMemberUUID(NuFactions nu_factions, Player player, boolean add_to_active_members) {
    UUID player_uuid = player.getUniqueId();
    for (int i = 0; i < nu_factions.factions.size(); i++) {
      Faction f = nu_factions.factions.get(i);
      for (int k = 0; k < f.faction_members.size(); k++) {
        UUID p_uuid = f.faction_members.get(k);
        if (p_uuid.toString().equalsIgnoreCase(player_uuid.toString())) {
          if (add_to_active_members) {
            for (int j = 0; j < f.active_members.size(); j++) {
              f.active_members.get(j).sendMessage(Utils.formatText("&a" + player.getName() + " has come online!"));
            }
            f.active_members.add(player);
          }
          return f;
        }
      }
    }
    return null;
  }

  public static ArrayList<Faction> generateFromSaveFile(YamlConfiguration factions_data) {
    ArrayList<Faction> factions = new ArrayList<Faction>();
    for (String id : factions_data.getKeys(false)) {
      String name = factions_data.getString(id + ".name");
      List<String> members = factions_data.getStringList(id + ".members");
      UUID leader = UUID.fromString(factions_data.getString(id + ".leader"));
      long power = factions_data.getLong(id + ".power");
      ArrayList<UUID> members_uuids = new ArrayList<UUID>();
      Faction faction = new Faction(new ArrayList<>(), members_uuids, name, leader, power);
      for (String member : members) {
        members_uuids.add(UUID.fromString(member));
        for (Player ply : Bukkit.getOnlinePlayers()) {
          if (ply.getUniqueId().toString().equalsIgnoreCase(member)) {
            faction.active_members.add(ply); 
          }
        }
      }
      ArrayList<Claim> claims = new ArrayList<Claim>();
      if (factions_data.getConfigurationSection(id + ".claims") != null) {
        for (String claim : factions_data.getConfigurationSection(id + ".claims").getKeys(false)) {
          Vector3 start = new Vector3(factions_data.getInt(id + ".claims." + claim + ".start.x", 0), factions_data.getInt(id + ".claims." + claim + ".start.y", 0), factions_data.getInt(id + ".claims." + claim + ".start.z", 0));
          Vector3 end = new Vector3(factions_data.getInt(id + ".claims." + claim + ".end.x", 0), factions_data.getInt(id + ".claims." + claim + ".end.y", 0), factions_data.getInt(id + ".claims." + claim + ".end.z", 0));
          String claim_name = factions_data.getString(id + ".claims." + claim + ".name", "");
          claims.add(new Claim(start, end, claim_name));
        }
      }
      faction.faction_name = name;
      faction.faction_members = members_uuids;
      faction.faction_claims = claims;
      faction.faction_leader = leader;
      factions.add(faction);
    }
    return factions;
  }
}
