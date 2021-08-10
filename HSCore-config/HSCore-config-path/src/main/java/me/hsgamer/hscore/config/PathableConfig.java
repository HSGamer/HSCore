package me.hsgamer.hscore.config;

import me.hsgamer.hscore.config.comment.CommentType;
import me.hsgamer.hscore.config.comment.Commentable;

import java.util.Map;

/**
 * A {@link Config} implementation to load {@link ConfigPath} automatically
 */
public class PathableConfig implements Config, Commentable {
  private final Config config;

  /**
   * Create a pathable config
   *
   * @param config the original config
   */
  public PathableConfig(Config config) {
    this.config = config;
  }

  @Override
  public Object getOriginal() {
    return this.config.getOriginal();
  }

  @Override
  public Object get(String path, Object def) {
    return this.config.get(path, def);
  }

  @Override
  public void set(String path, Object value) {
    this.config.set(path, value);
  }

  @Override
  public boolean contains(String path) {
    return this.config.contains(path);
  }

  @Override
  public String getName() {
    return this.config.getName();
  }

  @Override
  public void addDefault(String path, Object value) {
    this.config.addDefault(path, value);
  }

  @Override
  public Map<String, Object> getValues(String path, boolean deep) {
    return this.config.getValues(path, deep);
  }

  @Override
  public void setup() {
    this.config.setup();
    PathLoader.loadPath(this);
    this.save();
  }

  @Override
  public void save() {
    this.config.save();
  }

  @Override
  public void reload() {
    this.config.reload();
    PathLoader.reloadPath(this);
  }

  @Override
  public Object normalize(Object object) {
    return this.config.normalize(object);
  }

  @Override
  public boolean isNormalizable(Object object) {
    return this.config.isNormalizable(object);
  }

  @Override
  public String getComment(String path, CommentType type) {
    if (config instanceof Commentable) {
      return ((Commentable) config).getComment(path, type);
    }
    return null;
  }

  @Override
  public void setComment(String path, String value, CommentType type) {
    if (config instanceof Commentable) {
      ((Commentable) config).setComment(path, value, type);
    }
  }
}
