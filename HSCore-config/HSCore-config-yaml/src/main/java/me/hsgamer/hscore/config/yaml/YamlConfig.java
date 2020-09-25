package me.hsgamer.hscore.config.yaml;

import me.hsgamer.hscore.config.BaseConfig;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;

/**
 * A JSON config file
 */
public class YamlConfig extends BaseConfig<YamlFile> {

  /**
   * Create a YAML config
   *
   * @param file the config file
   */
  public YamlConfig(@NotNull final File file) {
    super(file, file1 -> {
      final YamlFile yamlFile = new YamlFile(file1);
      try {
        yamlFile.loadWithComments();
      } catch (InvalidConfigurationException | IOException e) {
        e.printStackTrace();
      }
      return yamlFile;
    });
  }

  /**
   * Create a YAML config
   *
   * @param dataFolder the data folder of the file
   * @param filename   the file name
   */
  public YamlConfig(@NotNull final File dataFolder, @NotNull final String filename) {
    this(new File(dataFolder, filename));
  }
}
