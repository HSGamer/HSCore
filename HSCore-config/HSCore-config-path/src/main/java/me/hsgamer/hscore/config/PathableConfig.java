package me.hsgamer.hscore.config;

import java.util.Map;

/**
 * A {@link Config} implementation to load {@link ConfigPath} automatically
 */
public class PathableConfig implements Config {
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
    return config.getOriginal();
  }

  @Override
  public Object get(String path, Object def) {
    return config.get(path, def);
  }

  @Override
  public void set(String path, Object value) {
    config.set(path, value);
  }

  @Override
  public boolean contains(String path) {
    return config.contains(path);
  }

  @Override
  public String getName() {
    return config.getName();
  }

  @Override
  public void addDefault(String path, Object value) {
    config.addDefault(path, value);
  }

  @Override
  public Map<String, Object> getValues(String path, boolean deep) {
    return config.getValues(path, deep);
  }

  @Override
  public void setup() {
    config.setup();
    PathLoader.loadPath(this);
    save();
  }

  @Override
  public void save() {
    config.save();
  }

  @Override
  public void reload() {
    config.reload();
  }

  @Override
  public Object normalize(Object object) {
    return config.normalize(object);
  }

  @Override
  public boolean isNormalizable(Object object) {
    return config.isNormalizable(object);
  }
}
