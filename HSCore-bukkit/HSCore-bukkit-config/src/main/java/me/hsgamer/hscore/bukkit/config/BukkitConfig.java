package me.hsgamer.hscore.bukkit.config;

import me.hsgamer.hscore.config.Config;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

/**
 * The bukkit configuration
 */
public class BukkitConfig implements Config {
  private final File file;
  private YamlConfiguration configuration;

  /**
   * Create a new config
   *
   * @param file the file
   */
  public BukkitConfig(File file) {
    this.file = file;
    setup();
  }

  /**
   * Create a new config
   *
   * @param plugin   the plugin
   * @param filename the file name
   */
  public BukkitConfig(Plugin plugin, String filename) {
    this(new File(plugin.getDataFolder(), filename));
  }

  @Override
  public Object getOriginal() {
    return configuration;
  }

  @Override
  public Object get(String path, Object def) {
    return configuration.get(path, def);
  }

  @Override
  public void set(String path, Object value) {
    configuration.set(path, value);
  }

  @Override
  public boolean contains(String path) {
    return configuration.contains(path);
  }

  @Override
  public String getName() {
    return configuration.getName();
  }

  @Override
  public void addDefault(String path, Object value) {
    configuration.addDefault(path, value);
  }

  @Override
  public Map<String, Object> getValues(String path, boolean deep) {
    if (path == null || path.isEmpty()) {
      return configuration.getValues(deep);
    } else {
      return Optional.ofNullable(configuration.getConfigurationSection(path))
        .map(configurationSection -> configurationSection.getValues(deep))
        .orElse(Collections.emptyMap());
    }
  }

  @Override
  public Object normalize(Object object) {
    if (object instanceof ConfigurationSection) {
      return ((ConfigurationSection) object).getValues(false);
    }
    return object;
  }

  @Override
  public boolean isNormalizable(Object object) {
    return object instanceof ConfigurationSection;
  }

  @Override
  public void setup() {
    if (!file.exists()) {
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }
      try {
        file.createNewFile();
      } catch (IOException e) {
        LOGGER.log(Level.WARNING, e, () -> "Something wrong when creating " + file.getName());
      }
    }
    this.configuration = YamlConfiguration.loadConfiguration(file);
  }

  @Override
  public void save() {
    this.configuration.options().copyDefaults(true);
    try {
      this.configuration.save(file);
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, e, () -> "Something wrong when saving " + file.getName());
    }
  }

  @Override
  public void reload() {
    this.configuration = YamlConfiguration.loadConfiguration(file);
  }
}
