package me.hsgamer.hscore.config.yaml;

import me.hsgamer.hscore.config.BaseConfig;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;

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
    super(file, new YamlProvider());
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
