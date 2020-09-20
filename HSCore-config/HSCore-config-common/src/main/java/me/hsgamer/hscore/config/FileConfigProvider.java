package me.hsgamer.hscore.config;

import java.io.File;
import java.io.IOException;
import org.simpleyaml.configuration.file.FileConfiguration;

/**
 * Config provider for {@link Config}
 */
public interface FileConfigProvider {

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
   * @param file the file
   * @throws IOException if there is an I/O error occurred
   */
  default void saveConfiguration(final FileConfiguration fileConfiguration, final File file)
    throws IOException {
    fileConfiguration.save(file);
  }

}
