package me.hsgamer.hscore.bukkit.config;

import me.hsgamer.hscore.config.BaseConfig;
import me.hsgamer.simplebukkityaml.configuration.file.YamlFile;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * A {@link me.hsgamer.hscore.config.Config} for Bukkit
 */
public class PluginConfig extends BaseConfig<YamlFile> {
  /**
   * Create a config with a provider
   *
   * @param file the config file
   */
  public PluginConfig(@NotNull File file) {
    super(file, file1 -> {
      final YamlFile yamlFile = new YamlFile(file1);
      try {
        yamlFile.loadWithComments();
      } catch (InvalidConfigurationException | IOException e) {
        LOGGER.log(Level.WARNING, "Error when loading yaml file", e);
      }
      return yamlFile;
    });
  }
}
