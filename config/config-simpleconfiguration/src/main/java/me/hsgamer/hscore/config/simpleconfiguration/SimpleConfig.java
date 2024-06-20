package me.hsgamer.hscore.config.simpleconfiguration;

import me.hsgamer.hscore.config.CommentType;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.PathString;
import me.hsgamer.hscore.logger.common.LogLevel;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * The {@link Config} implementation for SimpleYAML
 */
public class SimpleConfig<T extends FileConfiguration> implements Config {
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
      } catch (IOException e) {
        LOGGER.log(LogLevel.WARN, "Something wrong when loading " + file1.getName(), e);
      }
    });
  }

  private String toPath(String... path) {
    return PathString.join(String.valueOf(configuration.options().pathSeparator()), path);
  }

  private Map<String[], Object> toPathStringMap(Map<String, Object> map) {
    return PathString.split(String.valueOf(configuration.options().pathSeparator()), map);
  }

  @Override
  public FileConfiguration getOriginal() {
    return this.configuration;
  }

  @Override
  public Object get(Object def, String... path) {
    return this.configuration.get(toPath(path), def);
  }

  @Override
  public void set(Object value, String... path) {
    this.configuration.set(toPath(path), value);
  }

  @Override
  public boolean contains(String... path) {
    return this.configuration.isSet(toPath(path));
  }

  @Override
  public String getName() {
    return this.file.getName();
  }

  @Override
  public Map<String[], Object> getValues(boolean deep, String... path) {
    if (path.length == 0) {
      return toPathStringMap(this.configuration.getValues(deep));
    } else {
      return Optional.ofNullable(this.configuration.getConfigurationSection(toPath(path)))
        .map(configurationSection -> configurationSection.getValues(deep))
        .map(this::toPathStringMap)
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
    this.loader.accept(file, configuration);
  }

  @Override
  public void save() {
    try {
      this.configuration.save(this.file);
    } catch (IOException e) {
      LOGGER.log(LogLevel.WARN, "Something wrong when saving " + this.file.getName(), e);
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
  public List<String> getComment(CommentType type, String... path) {
    String comment = null;
    if (path.length == 0) {
      comment = configuration.options().header();
    } else if (configuration instanceof org.simpleyaml.configuration.comments.Commentable) {
      try {
        org.simpleyaml.configuration.comments.CommentType commentType = org.simpleyaml.configuration.comments.CommentType.valueOf(type.name());
        comment = ((org.simpleyaml.configuration.comments.Commentable) configuration).getComment(toPath(path), commentType);
      } catch (Exception e) {
        LOGGER.log(LogLevel.WARN, "Something wrong when getting comment of " + Arrays.toString(path), e);
      }
    }
    return comment == null ? Collections.emptyList() : Arrays.asList(comment.split("\\r?\\n"));
  }

  @Override
  public void setComment(CommentType type, List<String> value, String... path) {
    String comment = value == null || value.isEmpty() ? null : String.join("\n", value);
    if (path.length == 0) {
      configuration.options().header(comment).copyHeader(true);
    } else if (configuration instanceof org.simpleyaml.configuration.comments.Commentable) {
      try {
        org.simpleyaml.configuration.comments.CommentType commentType = org.simpleyaml.configuration.comments.CommentType.valueOf(type.name());
        ((org.simpleyaml.configuration.comments.Commentable) configuration).setComment(toPath(path), comment, commentType);
      } catch (Exception e) {
        LOGGER.log(LogLevel.WARN, "Something wrong when setting comment of " + path, e);
      }
    }
  }
}
