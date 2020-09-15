package me.hsgamer.hscore.bukkit.config;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Config provider for {@link PluginConfig}
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
   * @param file              the file
   * @throws IOException if there is an I/O error occurred
   */
  void saveConfiguration(FileConfiguration fileConfiguration, File file) throws IOException;
}
