package me.hsgamer.hscore.bukkit.config;

import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.ConfigOptions;
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
  private ConfigOptions options;
  private final File file;
  private YamlConfiguration configuration;

  /**
   * Create a new config
   *
   * @param file the file
   */
  public BukkitConfig(File file) {
    this.file = file;
  }

  /**
   * Create a new config
   *
   * @param plugin   the plugin
   * @param filename the file name
   */
  public BukkitConfig(Plugin plugin, String filename) {
    this(new File(plugin.getDataFolder(), filename));
    try {
      plugin.saveResource(filename, false);
    } catch (IllegalArgumentException e) {
      // IGNORED
    }
  }

  @Override
  public Object getOriginal() {
    return this.configuration;
  }

  @Override
  public ConfigOptions options() {
    if (options == null) {
      options = new ConfigOptions();
    }
    return options;
  }

  @Override
  public Object get(String path, Object def) {
    return this.configuration.get(path, def);
  }

  @Override
  public void set(String path, Object value) {
    this.configuration.set(path, value);
  }

  @Override
  public boolean contains(String path) {
    return this.configuration.isSet(path);
  }

  @Override
  public String getName() {
    return this.file.getName();
  }

  @Override
  public void addDefault(String path, Object value) {
    this.configuration.addDefault(path, value);
  }

  @Override
  public Map<String, Object> getValues(String path, boolean deep) {
    if (path == null || path.isEmpty()) {
      return this.configuration.getValues(deep);
    } else {
      return Optional.ofNullable(this.configuration.getConfigurationSection(path))
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
    if (!this.file.exists()) {
      File parentFile = this.file.getAbsoluteFile().getParentFile();
      if (!parentFile.exists()) {
        parentFile.mkdirs();
      }
      try {
        this.file.createNewFile();
      } catch (IOException e) {
        LOGGER.log(Level.WARNING, e, () -> "Something wrong when creating " + this.file.getName());
      }
    }
    this.configuration = YamlConfiguration.loadConfiguration(this.file);
    this.configuration.options().copyDefaults(true);
    this.configuration.options().pathSeparator(this.options().getPathSeparator());
    this.configuration.options().indent(this.options().getIndent());
  }

  @Override
  public void save() {
    try {
      this.configuration.save(this.file);
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, e, () -> "Something wrong when saving " + this.file.getName());
    }
  }

  @Override
  public void reload() {
    setup();
  }
}
