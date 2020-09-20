package me.hsgamer.hscore.config.yaml;

import java.io.File;
import me.hsgamer.hscore.config.ConfigEnvelope;
import org.simpleyaml.configuration.file.YamlConfiguration;

/**
 * A JSON config file
 */
public class YamlConfig extends ConfigEnvelope {

  /**
   * Create a YAML config
   *
   * @param file the config file
   */
  public YamlConfig(final File file) {
    super(file, YamlConfiguration::loadConfiguration);
  }

  /**
   * Create a YAML config
   *
   * @param dataFolder the data folder of the file
   * @param filename the file name
   */
  public YamlConfig(final File dataFolder, final String filename) {
    this(new File(dataFolder, filename));
  }

}
