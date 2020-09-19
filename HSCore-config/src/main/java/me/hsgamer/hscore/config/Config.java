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
  private final File configFile;
  private final FileConfigProvider provider;

  private FileConfiguration config;

  /**
   * Create a YAML plugin config
   *
   * @param dataFolder the data folder of the file
   * @param filename   the file name
   */
  public Config(File dataFolder, String filename) {
    this(new File(dataFolder, filename));
  }

  /**
   * Create a plugin config
   *
   * @param dataFolder the data folder of the file
   * @param filename   the file name
   * @param provider   the provider
   */
  public Config(File dataFolder, String filename, FileConfigProvider provider) {
    this(new File(dataFolder, filename), provider);
  }

  /**
   * Create a YAML plugin config
   *
   * @param file the config file
   */
  public Config(File file) {
    this(file, YamlConfiguration::loadConfiguration);
  }

  /**
   * Create a plugin config with a provider
   *
   * @param file     the config file
   * @param provider the provider
   */
  public Config(File file, FileConfigProvider provider) {
    this.configFile = file;
    this.provider = provider;
    setUpConfig();
  }

  /**
   * Set up the config
   */
  private void setUpConfig() {
    if (!configFile.getParentFile().exists()) {
      configFile.getParentFile().mkdirs();
    }

    if (!configFile.exists()) {
      try {
        configFile.createNewFile();
      } catch (IOException e) {
        LOGGER
          .log(Level.WARNING, e, () -> "Something wrong when creating " + getFileName());
      }
    }
    config = provider.loadConfiguration(configFile);
  }

  /**
   * Reload the config
   */
  public void reloadConfig() {
    config = provider.loadConfiguration(configFile);
  }

  /**
   * Save the config
   */
  public void saveConfig() {
    try {
      provider.saveConfiguration(config, configFile);
    } catch (IOException e) {
      LOGGER
        .log(Level.WARNING, e, () -> "Something wrong when saving " + getFileName());
    }
  }

  /**
   * Get the instance of the config file
   *
   * @return the config
   */
  public FileConfiguration getConfig() {
    if (config == null) {
      setUpConfig();
    }
    return config;
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
    return configFile.getName();
  }
}