package me.hsgamer.hscore.config.simpleconfiguration;

import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.comment.CommentType;
import me.hsgamer.hscore.config.comment.Commentable;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.logging.Level;

/**
 * The {@link Config} implementation for SimpleYAML
 */
public class SimpleConfig<T extends FileConfiguration> implements Config, Commentable {
  protected final BiConsumer<File, T> loader;
  private final File file;
  private final T configuration;

  /**
   * Create a new config
   *
   * @param file          the file
   * @param configuration the configuration
   * @param loader        the loader
   */
  public SimpleConfig(File file, T configuration, BiConsumer<File, T> loader) {
    this.file = file;
    this.configuration = configuration;
    this.loader = loader;
  }

  /**
   * Create a new config
   *
   * @param file          the file
   * @param configuration the configuration
   */
  public SimpleConfig(File file, T configuration) {
    this(file, configuration, (file1, t) -> {
      try {
        t.load(file1);
      } catch (IOException | InvalidConfigurationException e) {
        LOGGER.log(Level.WARNING, e, () -> "Something wrong when loading " + file1.getName());
      }
    });
  }


  @Override
  public FileConfiguration getOriginal() {
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
    this.configuration.options().copyDefaults(true);
    this.loader.accept(file, configuration);
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
    List<String> keys = new ArrayList<>(this.configuration.getKeys(false));
    keys.forEach(key -> this.configuration.set(key, null));
    setup();
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
  public String getComment(String path, CommentType type) {
    if (configuration instanceof org.simpleyaml.configuration.comments.Commentable) {
      try {
        org.simpleyaml.configuration.comments.CommentType commentType = org.simpleyaml.configuration.comments.CommentType.valueOf(type.name());
        return ((org.simpleyaml.configuration.comments.Commentable) configuration).getComment(path, commentType);
      } catch (Exception e) {
        // IGNORED
      }
    }
    return "";
  }

  @Override
  public void setComment(String path, String value, CommentType type) {
    if (configuration instanceof org.simpleyaml.configuration.comments.Commentable) {
      try {
        org.simpleyaml.configuration.comments.CommentType commentType = org.simpleyaml.configuration.comments.CommentType.valueOf(type.name());
        ((org.simpleyaml.configuration.comments.Commentable) configuration).setComment(path, value, commentType);
      } catch (Exception e) {
        // IGNORED
      }
    }
  }
}
