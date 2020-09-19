package me.hsgamer.hscore.config;

import java.io.File;

/**
 * A JSON config file
 */
public class YamlConfig extends Config {

  /**
   * Create a YAML config
   *
   * @param file the config file
   */
  public YamlConfig(File file) {
    super(file, FileType.YAML);
  }

  /**
   * Create a YAML config
   *
   * @param dataFolder the data folder of the file
   * @param filename   the file name
   */
  public YamlConfig(File dataFolder, String filename) {
    this(new File(dataFolder, filename));
  }
}
