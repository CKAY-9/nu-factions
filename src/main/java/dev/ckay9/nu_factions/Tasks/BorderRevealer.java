package dev.ckay9.nu_factions.Tasks;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import dev.ckay9.nu_factions.NuFactions;
import dev.ckay9.nu_factions.Factions.Claim;
import dev.ckay9.nu_factions.Factions.Faction;
import dev.ckay9.nu_factions.Utils.Utils;
import dev.ckay9.nu_factions.Utils.Vector3;

public class BorderRevealer {
    NuFactions factions;
    int task_id = 0;
    private ArrayList<Player> players_using_revealer = new ArrayList<>();

    private void setBlock(int x, int z, Player player, Material material) {
        int highest_y = player.getWorld().getHighestBlockYAt(x, z);
        Block block = player.getWorld().getBlockAt(x, highest_y, z);
        block.setType(material);
        player.sendBlockChange(new Location(player.getWorld(), x, highest_y, z), block.getBlockData());
    }

    private void cleanup(Player player) {
        for (int fi = 0; fi < this.factions.factions.size(); fi++) {
            Faction faction = this.factions.factions.get(fi);
            for (int ci = 0; ci < faction.faction_claims.size(); ci++) {
                Claim claim = faction.faction_claims.get(ci);
                Vector3 corner_b = new Vector3(claim.ending_position.x, 0, claim.starting_positon.z);
                Vector3 corner_d = new Vector3(claim.starting_positon.x, 0, claim.ending_position.z);
                for (int pi = 0; pi < players_using_revealer.size(); pi++) {
                    Player ply = players_using_revealer.get(pi);
                    for (int x1 = corner_b.x; x1 < claim.starting_positon.x; x1++) {
                        Block block_at = player.getWorld().getBlockAt(x1, player.getWorld().getHighestBlockYAt(x1, corner_b.z), corner_b.z);
                        setBlock(x1, corner_b.z, ply, block_at.getType());
                    }
                    for (int x2 = claim.ending_position.x; x2 < corner_d.x; x2++) {
                        Block block_at = player.getWorld().getBlockAt(x2, player.getWorld().getHighestBlockYAt(x2, corner_d.z), corner_d.z);
                        setBlock(x2, corner_d.z, ply, block_at.getType());
                    }
                    for (int z1 = corner_d.z; z1 < claim.starting_positon.z; z1++) {
                        Block block_at = player.getWorld().getBlockAt(corner_d.x, player.getWorld().getHighestBlockYAt(corner_d.x, z1), z1);
                        setBlock(corner_d.x, z1, ply, block_at.getType());
                    }
                    for (int z2 = claim.ending_position.z; z2 < corner_b.z; z2++) {
                        Block block_at = player.getWorld().getBlockAt(corner_b.x, player.getWorld().getHighestBlockYAt(corner_b.x, z2), z2);
                        setBlock(corner_b.x, z2, ply, block_at.getType());
                    }
                }
            }
        }
    }

    public BorderRevealer(NuFactions factions) {
        this.factions = factions;
        for (Player p : Bukkit.getOnlinePlayers()) {
            this.players_using_revealer.add(p);
        }
        task_id = this.factions.getServer().getScheduler().scheduleSyncRepeatingTask(Utils.getPlugin(), () -> {
            for (int fi = 0; fi < this.factions.factions.size(); fi++) {
                Faction faction = this.factions.factions.get(fi);
                for (int ci = 0; ci < faction.faction_claims.size(); ci++) {
                    Claim claim = faction.faction_claims.get(ci);
                    //
                    // corners:
                    //  (b)//////(a)
                    //  //        //
                    //  (c)//////(d)
                    // corner a and c already defined by claim
                    //
                    Vector3 corner_b = new Vector3(claim.ending_position.x, 0, claim.starting_positon.z);
                    Vector3 corner_d = new Vector3(claim.starting_positon.x, 0, claim.ending_position.z);
                    for (int pi = 0; pi < players_using_revealer.size(); pi++) {
                        Player ply = players_using_revealer.get(pi);
                        for (int x1 = corner_b.x; x1 < claim.starting_positon.x; x1++) {
                            setBlock(x1, corner_b.z, ply, Material.BEDROCK);
                        }
                        for (int x2 = claim.ending_position.x; x2 < corner_d.x; x2++) {
                            setBlock(x2, corner_d.z, ply, Material.BEDROCK);
                        }
                        for (int z1 = corner_d.z; z1 < claim.starting_positon.z; z1++) {
                            setBlock(corner_d.x, z1, ply, Material.BEDROCK);
                        }
                        for (int z2 = claim.ending_position.z; z2 < corner_b.z; z2++) {
                            setBlock(corner_b.x, z2, ply, Material.BEDROCK);
                        }

                        WorldBorder border = Bukkit.createWorldBorder();
                        border.setCenter(claim.getCenterCoordinates().x, claim.getCenterCoordinates().z);
                        border.setSize(claim.radius);
                        border.setDamageAmount(0);
                        border.setWarningDistance(0);
                        // TODO: Send via packets
                    }
                }
            }
        }, 0L, 5L);
    }

    public void toggleRevealer(Player player) {
        for (int i = 0; i < players_using_revealer.size(); i++) {
            Player p = players_using_revealer.get(i);
            if (player.getUniqueId() == p.getUniqueId()) {
                players_using_revealer.remove(p);
                cleanup(player);
                return;
            }
        }
        players_using_revealer.add(player);
    }
}
