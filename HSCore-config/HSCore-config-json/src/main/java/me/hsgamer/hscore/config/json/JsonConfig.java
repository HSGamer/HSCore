package me.hsgamer.hscore.config.json;

import io.github.portlek.jsongration.JsonConfiguration;
import me.hsgamer.hscore.config.Config;

import java.io.File;

/**
 * A JSON config file
 */
public class JsonConfig extends Config {

  /**
   * Create a JSON config
   *
   * @param file the config file
   */
  public JsonConfig(File file) {
    super(file, JsonConfiguration::loadConfiguration);
  }

  /**
   * Create a JSON config
   *
   * @param dataFolder the data folder of the file
   * @param filename   the file name
   */
  public JsonConfig(File dataFolder, String filename) {
    this(new File(dataFolder, filename));
  }
}
