package me.hsgamer.hscore.config;

import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple config file
 */
public class Config {

  private static final Logger LOGGER = Logger.getLogger("PluginConfig");
  private final File file;
  private final FileConfigProvider provider;

  private FileConfiguration fileConfiguration;

  /**
   * Create a YAML config
   *
   * @param dataFolder the data folder of the file
   * @param filename   the file name
   */
  public Config(File dataFolder, String filename) {
    this(new File(dataFolder, filename));
  }

  /**
   * Create a config
   *
   * @param dataFolder the data folder of the file
   * @param filename   the file name
   * @param provider   the provider
   */
  public Config(File dataFolder, String filename, FileConfigProvider provider) {
    this(new File(dataFolder, filename), provider);
  }

  /**
   * Create a YAML config
   *
   * @param file the config file
   */
  public Config(File file) {
    this(file, YamlConfiguration::loadConfiguration);
  }

  /**
   * Create a config with a provider
   *
   * @param file     the config file
   * @param provider the provider
   */
  public Config(File file, FileConfigProvider provider) {
    this.file = file;
    this.provider = provider;
    setUpConfig();
  }

  /**
   * Set up the config
   */
  private void setUpConfig() {
    if (!file.exists()) {
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }

      try {
        file.createNewFile();
      } catch (IOException e) {
        LOGGER.log(Level.WARNING, e, () -> "Something wrong when creating " + getFileName());
      }
    }
    fileConfiguration = provider.loadConfiguration(file);
  }

  /**
   * Reload the config
   */
  public void reloadConfig() {
    fileConfiguration = provider.loadConfiguration(file);
  }

  /**
   * Save the config
   */
  public void saveConfig() {
    try {
      provider.saveConfiguration(fileConfiguration, file);
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, e, () -> "Something wrong when saving " + getFileName());
    }
  }

  /**
   * Get the instance of the config file
   *
   * @return the config
   */
  public FileConfiguration getConfig() {
    if (fileConfiguration == null) {
      setUpConfig();
    }
    return fileConfiguration;
  }


  /**
   * Get the value from the config
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   * @return the value
   */
  public Object get(String path, Object def) {
    return getConfig().get(path, def);
  }

  /**
   * Get the value from the config
   *
   * @param path the path to the value
   * @return the value
   */
  public Object get(String path) {
    return get(path, null);
  }

  /**
   * Get the file name
   *
   * @return the file name
   */
  public String getFileName() {
    return file.getName();
  }
}