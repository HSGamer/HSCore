package me.hsgamer.hscore.bukkit.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import me.hsgamer.hscore.bukkit.config.provider.YamlConfigProvider;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A simple config file
 */
public class PluginConfig {

  private final File configFile;
  private final JavaPlugin plugin;
  private final String fileName;
  private final FileConfigProvider provider;

  private FileConfiguration config;

  /**
   * Create a YAML plugin config
   *
   * @param plugin   the plugin
   * @param filename the file name
   */
  public PluginConfig(JavaPlugin plugin, String filename) {
    this(plugin, new File(plugin.getDataFolder(), filename));
  }

  /**
   * Create a YAML plugin config
   *
   * @param plugin the plugin
   * @param file   the config file
   */
  public PluginConfig(JavaPlugin plugin, File file) {
    this(plugin, file, new YamlConfigProvider());
  }

  /**
   * Create a plugin config with a provider
   *
   * @param plugin   the plugin
   * @param file     the config file
   * @param provider the provider
   */
  public PluginConfig(JavaPlugin plugin, File file, FileConfigProvider provider) {
    this.plugin = plugin;
    this.configFile = file;
    this.fileName = file.getName();
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
        plugin.getLogger().log(Level.WARNING, e, () -> "Something wrong when creating " + fileName);
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
      plugin.getLogger().log(Level.WARNING, e, () -> "Something wrong when saving " + fileName);
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
    return fileName;
  }
}