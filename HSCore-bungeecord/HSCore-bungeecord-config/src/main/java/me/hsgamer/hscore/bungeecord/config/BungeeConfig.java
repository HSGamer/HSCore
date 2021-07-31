package me.hsgamer.hscore.bungeecord.config;

import me.hsgamer.hscore.config.Config;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

/**
 * The BungeeCord Configuration
 */
public class BungeeConfig implements Config {
  private final File file;
  private Configuration configuration;

  /**
   * Create a new config
   *
   * @param file the file
   */
  public BungeeConfig(File file) {
    this.file = file;
  }

  /**
   * Create a new config
   *
   * @param plugin   the plugin
   * @param filename the file name
   */
  public BungeeConfig(Plugin plugin, String filename) {
    this(new File(plugin.getDataFolder(), filename));
  }

  @Override
  public Configuration getOriginal() {
    return this.configuration;
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
    return this.configuration.contains(path);
  }

  @Override
  public String getName() {
    return this.file.getName();
  }

  @Override
  public Map<String, Object> getValues(String path, boolean deep) {
    if (path == null || path.isEmpty()) {
      return this.getValues(configuration, deep);
    } else {
      return Optional.ofNullable(configuration.getSection(path))
        .map(section -> this.getValues(section, deep))
        .orElse(Collections.emptyMap());
    }
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
    try {
      this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, e, () -> "Something wrong when loading " + this.file.getName());
    }
  }

  @Override
  public void save() {
    try {
      ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.configuration, this.file);
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, e, () -> "Something wrong when saving " + this.file.getName());
    }
  }

  @Override
  public void reload() {
    this.setup();
  }

  @Override
  public Object normalize(Object object) {
    if (object instanceof Configuration) {
      return this.getValues((Configuration) object, false);
    }
    return object;
  }

  @Override
  public boolean isNormalizable(Object object) {
    return object instanceof Configuration;
  }

  private Map<String, Object> getValues(Configuration section, boolean deep) {
    Collection<String> keys = section.getKeys();
    if (!deep) {
      keys.removeIf(s -> s.indexOf('.') < 0);
    }
    Map<String, Object> values = new LinkedHashMap<>();
    for (String key : keys) {
      values.put(key, section.get(key));
    }
    return values;
  }
}
