package me.hsgamer.hscore.config;

import io.github.portlek.jsongration.JsonConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;

/**
 * File types for {@link FileConfigProvider}
 */
public enum FileType {

  YAML(YamlConfiguration::loadConfiguration),
  JSON(JsonConfiguration::loadConfiguration);

  private final FileConfigProvider provider;

  FileType(FileConfigProvider provider) {
    this.provider = provider;
  }

  public FileConfigProvider getProvider() {
    return provider;
  }
}
