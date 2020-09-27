package me.hsgamer.hscore.bukkit.config;

import me.hsgamer.hscore.bukkit.config.file.BukkitYamlConfiguration;
import me.hsgamer.hscore.config.BaseConfig;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * A {@link me.hsgamer.hscore.config.Config} for Bukkit
 */
public class PluginConfig extends BaseConfig<BukkitYamlConfiguration> {
  /**
   * Create a config with a provider
   *
   * @param file the config file
   */
  public PluginConfig(@NotNull File file) {
    super(file, BukkitYamlConfiguration::loadConfiguration);
  }
}
