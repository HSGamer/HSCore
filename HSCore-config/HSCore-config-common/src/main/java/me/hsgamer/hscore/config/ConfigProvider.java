package me.hsgamer.hscore.config;

import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Config provider for {@link Config}
 */
public interface ConfigProvider<FC extends FileConfiguration> {

  /**
   * Load configuration from a file
   *
   * @param file the file
   * @return the configuration
   */
  FC loadConfiguration(File file);

  /**
   * Save the configuration to the file
   *
   * @param fileConfiguration the configuration
   * @param file              the file
   * @throws IOException if there is an I/O error occurred
   */
  default void saveConfiguration(FC fileConfiguration, File file)
    throws IOException {
    fileConfiguration.save(file);
  }
}
