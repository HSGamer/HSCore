package me.hsgamer.hscore.config.yaml;

import me.hsgamer.hscore.config.BaseConfig;
import me.hsgamer.hscore.config.ConfigProvider;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * a YAML implementation for {@link ConfigProvider}.
 */
public final class YamlProvider implements ConfigProvider<YamlFile> {

  @NotNull
  @Override
  public YamlFile loadConfiguration(@NotNull final File file) {
    final YamlFile yamlFile = new YamlFile(file);
    try {
      yamlFile.loadWithComments();
    } catch (InvalidConfigurationException | IOException e) {
      BaseConfig.LOGGER.log(Level.WARNING, "Error when loading yaml file", e);
    }
    return yamlFile;
  }
}
