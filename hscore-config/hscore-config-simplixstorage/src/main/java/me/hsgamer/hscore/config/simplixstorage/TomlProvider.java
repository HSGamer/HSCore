package me.hsgamer.hscore.config.simplixstorage;

import de.leonhard.storage.Toml;
import me.hsgamer.hscore.config.ConfigProvider;

import java.io.File;

/**
 * The TOML provider
 */
public class TomlProvider implements ConfigProvider<LightningConfig<Toml>> {
  @Override
  public LightningConfig<Toml> loadConfiguration(File file) {
    return new LightningConfig<>(new Toml(file));
  }
}
