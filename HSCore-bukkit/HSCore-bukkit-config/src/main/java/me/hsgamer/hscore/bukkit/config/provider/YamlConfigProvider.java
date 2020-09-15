package me.hsgamer.hscore.bukkit.config.provider;

import java.io.File;
import me.hsgamer.hscore.bukkit.config.FileConfigProvider;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class YamlConfigProvider implements FileConfigProvider {

  @Override
  public FileConfiguration loadConfiguration(File file) {
    return YamlConfiguration.loadConfiguration(file);
  }
}
