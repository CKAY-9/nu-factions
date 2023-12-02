package dev.ckay9.nu_factions.Factions;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Utils.Utils;
import dev.ckay9.nu_factions.Utils.Vector3;

public class Faction {
  public ArrayList<Claim> faction_claims = new ArrayList<Claim>();
  public String faction_name;
  public ArrayList<UUID> faction_members = new ArrayList<UUID>();
  public ArrayList<Player> active_members = new ArrayList<Player>();

  public Faction(ArrayList<Claim> claims, ArrayList<UUID> members, String name) {
    this.faction_members = members;
    this.faction_name = name;
    this.faction_claims = claims;

    for (Player p : Bukkit.getOnlinePlayers()) {
      for (int i = 0; i < this.faction_members.size(); i++) {
        if (p.getUniqueId() == this.faction_members.get(i)) {
          this.active_members.add(p);
        }
      }
    }
  }

  // idk
  public static Faction getFactionFromMemberUUID(NuFactions nu_factions, Player player, boolean add_to_active_members) {
    UUID player_uuid = player.getUniqueId();
    for (int i = 0; i < nu_factions.factions.size(); i++) {
      Faction f = nu_factions.factions.get(i);
      for (int k = 0; k < f.faction_members.size(); k++) {
        UUID p_uuid = f.faction_members.get(k);
        if (p_uuid == player_uuid) {
          for (int j = 0; j < f.active_members.size(); j++) {
            f.active_members.get(j).sendMessage(Utils.formatText("&a" + player.getName() + " has come online!"));
          }
          if (add_to_active_members) {
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
      ArrayList<UUID> members_uuids = new ArrayList<UUID>();

      for (String member : members) {
        members_uuids.add(UUID.fromString(member));
      }

      ArrayList<Claim> claims = new ArrayList<Claim>();

      for (String claim : factions_data.getConfigurationSection(id + ".claims").getKeys(false)) {
        Vector3 start = new Vector3(factions_data.getInt(id + ".claims" + claim + ".start.x", 0), factions_data.getInt(id + ".claims" + claim + ".start.y", 0), factions_data.getInt(id + ".claims" + claim + ".start.z", 0));
        Vector3 end = new Vector3(factions_data.getInt(id + ".claims" + claim + ".end.x", 0), factions_data.getInt(id + ".claims" + claim + ".end.y", 0), factions_data.getInt(id + ".claims" + claim + ".end.z", 0));
        String claim_name = factions_data.getString(id + ".claims" + claim + ".name", "");
        claims.add(new Claim(start, end, claim_name));
      }
      factions.add(new Faction(claims, members_uuids, name));
    }
    return factions;
  }
}
