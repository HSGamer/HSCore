package me.hsgamer.hscore.bukkit.config;

import me.hsgamer.hscore.config.ConfigProvider;

import java.io.File;

/**
 * The provider for {@link BukkitConfig}
 */
public class BukkitConfigProvider implements ConfigProvider<BukkitConfig> {
  @Override
  public BukkitConfig loadConfiguration(File file) {
    return new BukkitConfig(file);
  }
}
