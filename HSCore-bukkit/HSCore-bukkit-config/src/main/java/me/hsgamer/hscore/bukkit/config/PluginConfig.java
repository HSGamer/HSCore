package me.hsgamer.hscore.bukkit.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A simple config file
 */
public class PluginConfig {

  private final File configFile;
  private final JavaPlugin plugin;
  private final String fileName;
  private FileConfiguration config;

  /**
   * Create a plugin config
   *
   * @param plugin   the plugin
   * @param filename the file name
   */
  public PluginConfig(JavaPlugin plugin, String filename) {
    this(plugin, new File(plugin.getDataFolder(), filename));
  }

  /**
   * Create a plugin config
   *
   * @param plugin the plugin
   * @param file   the config file
   */
  public PluginConfig(JavaPlugin plugin, File file) {
    this.plugin = plugin;
    this.configFile = file;
    this.fileName = file.getName();
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
    config = YamlConfiguration.loadConfiguration(configFile);
  }

  /**
   * Reload the config
   */
  public void reloadConfig() {
    config = YamlConfiguration.loadConfiguration(configFile);
  }

  /**
   * Save the config
   */
  public void saveConfig() {
    try {
      config.save(configFile);
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
   * @param <T>       the type of the value
   * @param classType the class type of value
   * @param path      the path to the value
   * @param def       the default value if it's not found
   * @return the value
   */
  @SuppressWarnings("unchecked")
  public <T> T get(Class<T> classType, String path, T def) {
    Object o = getConfig().get(path, def);
    return classType.isInstance(o) ? (T) o : def;
  }

  /**
   * Get the value from the config
   *
   * @param <T>       the type of the value
   * @param classType the class type of value
   * @param path      the path to the value
   * @return the value
   */
  public <T> T get(Class<T> classType, String path) {
    return get(classType, path, null);
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