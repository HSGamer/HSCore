package me.hsgamer.hscore.bungeecord.config;

import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.PathString;
import me.hsgamer.hscore.logger.common.LogLevel;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static me.hsgamer.hscore.config.PathString.joinDefault;
import static me.hsgamer.hscore.config.PathString.splitDefault;

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
  public Object get(Object def, String... path) {
    return this.configuration.get(joinDefault(path), def);
  }

  @Override
  public void set(Object value, String... path) {
    this.configuration.set(joinDefault(path), value);
  }

  @Override
  public boolean contains(String... path) {
    return this.configuration.contains(joinDefault(path));
  }

  @Override
  public String getName() {
    return this.file.getName();
  }

  @Override
  public Map<String[], Object> getValues(boolean deep, String... path) {
    if (path.length == 0) {
      return splitDefault(this.getValues(configuration, deep));
    } else {
      return Optional.ofNullable(configuration.getSection(joinDefault(path)))
        .map(section -> this.getValues(section, deep))
        .map(PathString::splitDefault)
        .orElse(Collections.emptyMap());
    }
  }

  @Override
  public void setup() {
    if (!this.file.exists()) {
      File parentFile = this.file.getAbsoluteFile().getParentFile();
      if (parentFile != null && !parentFile.exists()) {
        parentFile.mkdirs();
      }
      try {
        this.file.createNewFile();
      } catch (IOException e) {
        LOGGER.log(LogLevel.WARN, "Something wrong when creating " + this.file.getName(), e);
      }
    }
    try {
      this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
    } catch (IOException e) {
      LOGGER.log(LogLevel.WARN, "Something wrong when loading " + this.file.getName(), e);
    }
  }

  @Override
  public void clear() {
    this.configuration = new Configuration();
  }

  @Override
  public void save() {
    try {
      ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.configuration, this.file);
    } catch (IOException e) {
      LOGGER.log(LogLevel.WARN, "Something wrong when saving " + this.file.getName(), e);
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
