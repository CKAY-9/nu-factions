package dev.ckay9.nu_factions.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Claim;
import dev.ckay9.nu_factions.Factions.Faction;
import dev.ckay9.nu_factions.Factions.FactionClaim;
import dev.ckay9.nu_factions.Utils.Utils;

public class FactionGUI {
  NuFactions factions;
  int task_id = 0;

  private void handleGUI(Player player, ScoreboardManager sb_manager) {
    if (sb_manager == null) {
      return;
    } 

    Scoreboard board = sb_manager.getNewScoreboard();
    Objective obj = board.registerNewObjective("NuFactions-Main", "dummny", Utils.formatText("&6Nu-Factions"));
    obj.setDisplaySlot(DisplaySlot.SIDEBAR);

    int running_score = 100;
    
    Faction faction = Faction.getFactionFromMemberUUID(this.factions, player, false);
    String faction_name = "None";
    if (faction != null) {
      faction_name = faction.faction_name;
    }

    Score your_faction = obj.getScore(Utils.formatText("&5Current Faction: " + faction_name));
    your_faction.setScore(running_score--);

    if (faction != null) {
      Score your_power = obj.getScore(Utils.formatText("&5Faction Power: " + faction.faction_power));
      your_power.setScore(running_score--);
      Score members = obj.getScore(Utils.formatText("&5Active Members: " + faction.faction_members.size()));
      members.setScore(running_score--);
    }

    FactionClaim current_claim = Claim.getCurrentPlayerClaim(player, this.factions);
    if (current_claim != null) {
      Score claim_border = obj.getScore(Utils.formatText(" "));
      claim_border.setScore(running_score--);
      Score claim_score = obj.getScore(Utils.formatText("&cCurrent Claim: " + current_claim.claim.claim_name));
      claim_score.setScore(running_score--);
      Score claim_owner = obj.getScore(Utils.formatText("&cControlled by " + current_claim.faction.faction_name));
      claim_owner.setScore(running_score--);
    }

    player.setScoreboard(board);
  }

  public FactionGUI(NuFactions factions) {
    this.factions = factions;

    task_id = this.factions.getServer().getScheduler().scheduleSyncRepeatingTask(this.factions, () -> {
      ScoreboardManager manager = Bukkit.getScoreboardManager();
      for (Player player : Bukkit.getOnlinePlayers()) {
        handleGUI(player, manager);    
      }
    }, 0L, 20L);
  }
}
