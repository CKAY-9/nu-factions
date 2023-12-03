package dev.ckay9.nu_factions;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import dev.ckay9.nu_factions.Utils.Utils;

public class Data {
  public static File factions_file;
  public static YamlConfiguration factions_data; 

  public static void initializeDataFiles() {
    try {
      factions_file = new File(Utils.getPlugin().getDataFolder(), "factions_data.yml");
      if (!factions_file.exists()) {
        if (factions_file.getParentFile().mkdirs()) {
          Utils.getPlugin().getLogger().info("Created data folder");
        }
        if (factions_file.createNewFile()) {
          Utils.getPlugin().getLogger().info("Created factions data file");
        }
      }

      factions_data = YamlConfiguration.loadConfiguration(factions_file);
    } catch (IOException ex) {
      Utils.getPlugin().getLogger().warning(ex.toString());
    }
  }
}
