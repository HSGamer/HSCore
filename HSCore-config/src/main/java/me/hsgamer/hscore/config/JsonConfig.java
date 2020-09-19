package me.hsgamer.hscore.config;

import java.io.File;

/**
 * A YAML config file
 */
public class JsonConfig extends Config {

  /**
   * Create a JSON config
   *
   * @param file the config file
   */
  public JsonConfig(File file) {
    super(file, FileType.JSON);
  }

  /**
   * Create a YAML config
   *
   * @param dataFolder the data folder of the file
   * @param filename   the file name
   */
  public JsonConfig(File dataFolder, String filename) {
    this(new File(dataFolder, filename));
  }
}
