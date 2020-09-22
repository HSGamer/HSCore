package me.hsgamer.hscore.config;

import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Config provider for {@link BaseConfig}
 */
public interface ConfigProvider {

  /**
   * Load configuration from a file
   *
   * @param file the file
   * @return the configuration
   */
  FileConfiguration loadConfiguration(File file);

  /**
   * Save the configuration to the file
   *
   * @param fileConfiguration the configuration
   * @param file              the file
   * @throws IOException if there is an I/O error occurred
   */
  default void saveConfiguration(FileConfiguration fileConfiguration, File file)
    throws IOException {
    fileConfiguration.save(file);
  }
}
