package me.hsgamer.hscore.config;

import java.util.logging.Logger;
import org.simpleyaml.configuration.file.FileConfiguration;

public interface Config {

  Logger LOGGER = Logger.getLogger("Config");

  /**
   * Reload the config
   */
  void reloadConfig();

  /**
   * Save the config
   */
  void saveConfig();

  /**
   * Get the instance of the config file
   *
   * @return the config
   */
  FileConfiguration getConfig();

  /**
   * Get the value from the config
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   * @return the value
   */
  default Object get(final String path, final Object def) {
    return this.getConfig().get(path, def);
  }

  /**
   * Get the value from the config
   *
   * @param path the path to the value
   * @return the value
   */
  default Object get(final String path) {
    return this.get(path, null);
  }

  /**
   * Get the file name
   *
   * @return the file name
   */
  String getFileName();

}
