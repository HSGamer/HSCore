package me.hsgamer.hscore.config.simpleconfiguration;

import me.hsgamer.hscore.config.Config;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;

/**
 * The {@link Config} implementation for SimpleYAML
 */
public class SimpleConfig implements Config {
  private final File file;
  private final Function<File, FileConfiguration> loader;
  private FileConfiguration configuration;

  /**
   * Create a new config
   *
   * @param file   the file
   * @param loader the loader
   */
  public SimpleConfig(File file, Function<File, FileConfiguration> loader) {
    this.file = file;
    this.loader = loader;
    setup();
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
    return file.getName();
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
    this.configuration = loader.apply(file);
    this.configuration.options().copyDefaults(true);
  }

  @Override
  public void save() {
    try {
      this.configuration.save(file);
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, e, () -> "Something wrong when saving " + file.getName());
    }
  }

  @Override
  public void reload() {
    this.configuration = loader.apply(file);
    this.configuration.options().copyDefaults(true);
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
}
