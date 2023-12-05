package dev.ckay9.nu_factions;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import dev.ckay9.nu_factions.Utils.Utils;

public class Data {
  public static File factions_file;
  public static YamlConfiguration factions_data; 

  public static File config_file;
  public static YamlConfiguration config_data;

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

  public static void initializeConfigFiles() {
    try {
      config_file = new File(Utils.getPlugin().getDataFolder(), "config.yml");
      if (!config_file.exists()) {
        if (config_file.getParentFile().mkdirs()) {
          Utils.getPlugin().getLogger().info("Created data folder");
        }
        if (config_file.createNewFile()) {
          Utils.getPlugin().getLogger().info("Created config file");
        }
      }

      config_data = YamlConfiguration.loadConfiguration(config_file);
      
      if (!config_data.isSet("config.show_right_gui")) {
        config_data.set("config.show_right_gui", true);
      }
      if (!config_data.isSet("config.claim_addition_cost_percentage")) {
        config_data.set("config.claim_addition_cost_percentage", 0.1);
      }
      if (!config_data.isSet("config.show_faction_names")) {
        config_data.set("config.show_faction_names", true);
      }
      if (!config_data.isSet("config.claim_decay_active")) {
        config_data.set("config.claim_decay_active", true);
      }
      if (!config_data.isSet("config.claim_decay_in_minutes")) {
        config_data.set("config.claim_decay_in_minutes", 30);
      }
      if (!config_data.isSet("config.entity_power_reward")) {
        config_data.set("config.entity_power_reward", 5);
      }
      if (!config_data.isSet("config.player_power_reward")) {
        config_data.set("config.player_power_reward", 20);
      }
      
      config_data.save(config_file);
    } catch (IOException ex) {
      Utils.getPlugin().getLogger().warning(ex.toString());
    }
  }
}
