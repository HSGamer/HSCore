package me.hsgamer.hscore.bukkit.config;

import me.hsgamer.hscore.config.BaseConfig;
import me.hsgamer.hscore.config.ConfigProvider;
import me.hsgamer.simplebukkityaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Bukkit's YAML implementation for {@link ConfigProvider}.
 */
public class PluginYamlProvider implements ConfigProvider<YamlFile> {
  @Override
  public YamlFile loadConfiguration(File file) {
    final YamlFile yamlFile = new YamlFile(file);
    try {
      yamlFile.loadWithComments();
    } catch (InvalidConfigurationException | IOException e) {
      BaseConfig.LOGGER.log(Level.WARNING, "Error when loading yaml file", e);
    }
    return yamlFile;
  }
}
