package dev.ckay9.nu_factions;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class Data {
  public static File factionsFile;
  public static YamlConfiguration factionsData; 

  public static void initializeDataFiles() {
    try {
      factionsFile = new File(Utils.getPlugin().getDataFolder(), "factions_data.yml");
      if (!factionsFile.exists()) {
        if (factionsFile.getParentFile().mkdirs()) {
          Utils.getPlugin().getLogger().info("Created data folder");
        }
        if (factionsFile.createNewFile()) {
          Utils.getPlugin().getLogger().info("Created factions data file");
        }
      }

      factionsData = YamlConfiguration.loadConfiguration(factionsFile);
    } catch (IOException ex) {
      Utils.getPlugin().getLogger().warning(ex.toString());
    }
  }
}
