package me.hsgamer.hscore.config.json;

import io.github.portlek.jsongration.JsonConfiguration;
import java.io.File;
import me.hsgamer.hscore.config.ConfigEnvelope;

/**
 * A JSON config file
 */
public class JsonConfig extends ConfigEnvelope {

  /**
   * Create a JSON config
   *
   * @param file the config file
   */
  public JsonConfig(final File file) {
    super(file, JsonConfiguration::loadConfiguration);
  }

  /**
   * Create a JSON config
   *
   * @param dataFolder the data folder of the file
   * @param filename the file name
   */
  public JsonConfig(final File dataFolder, final String filename) {
    this(new File(dataFolder, filename));
  }

}
