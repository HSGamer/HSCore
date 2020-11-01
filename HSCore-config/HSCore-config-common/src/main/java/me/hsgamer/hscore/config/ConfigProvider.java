package me.hsgamer.hscore.config;

import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Config provider for {@link Config}
 *
 * @param <F> File configuration class type
 */
public interface ConfigProvider<F extends FileConfiguration> {

  /**
   * Load configuration from a file
   *
   * @param file the file
   *
   * @return the configuration
   */
  F loadConfiguration(File file);

  /**
   * Load configuration from an input stream
   *
   * @param inputStream the input stream
   *
   * @return the configuration
   *
   * @throws IOException if there is an I/O error
   */
  default F loadConfiguration(InputStream inputStream) throws IOException {
    File file = File.createTempFile("tempAddonConfig", null);
    file.deleteOnExit();
    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    return loadConfiguration(file);
  }

  /**
   * Save the configuration to the file
   *
   * @param fileConfiguration the configuration
   * @param file              the file
   *
   * @throws IOException if there is an I/O error occurred
   */
  default void saveConfiguration(F fileConfiguration, File file) throws IOException {
    fileConfiguration.save(file);
  }
}
