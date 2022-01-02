package me.hsgamer.hscore.bungeecord.config;

import me.hsgamer.hscore.config.ConfigProvider;

import java.io.File;

/**
 * The provider for {@link BungeeConfig}
 */
public class BungeeConfigProvider implements ConfigProvider<BungeeConfig> {
  @Override
  public BungeeConfig loadConfiguration(File file) {
    return new BungeeConfig(file);
  }
}
