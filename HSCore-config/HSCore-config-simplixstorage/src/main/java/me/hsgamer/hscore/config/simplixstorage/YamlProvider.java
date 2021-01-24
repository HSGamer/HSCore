package me.hsgamer.hscore.config.simplixstorage;

import de.leonhard.storage.Yaml;
import me.hsgamer.hscore.config.ConfigProvider;

import java.io.File;

/**
 * The YAML provider
 */
public class YamlProvider implements ConfigProvider<LightningConfig<Yaml>> {
  @Override
  public LightningConfig<Yaml> loadConfiguration(File file) {
    return new LightningConfig<>(new Yaml(file));
  }
}
