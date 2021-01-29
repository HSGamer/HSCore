package me.hsgamer.hscore.config.simpleconfiguration;

import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.comment.CommentType;
import me.hsgamer.hscore.config.comment.Commentable;
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
public class SimpleConfig implements Config, Commentable {
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
  }

  @Override
  public Object getOriginal() {
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
      if (!this.file.getParentFile().exists()) {
        this.file.getParentFile().mkdirs();
      }
      try {
        this.file.createNewFile();
      } catch (IOException e) {
        LOGGER.log(Level.WARNING, e, () -> "Something wrong when creating " + this.file.getName());
      }
    }
    this.configuration = this.loader.apply(this.file);
    this.configuration.options().copyDefaults(true);
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
