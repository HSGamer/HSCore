package me.hsgamer.hscore.config;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A decorative {@link Config} for extending more features on the existing {@link Config}
 */
public abstract class DecorativeConfig implements Config {
  protected final Config config;

  /**
   * Create a new decorative config
   *
   * @param config the original config
   */
  protected DecorativeConfig(Config config) {
    this.config = config;
  }

  /**
   * Get the original config
   *
   * @return the config
   */
  public Config getOriginalConfig() {
    return this.config;
  }

  @Override
  public Object getOriginal() {
    return config.getOriginal();
  }

  @Override
  public Object get(Object def, String... path) {
    return config.get(def, path);
  }

  @Override
  public void set(Object value, String... path) {
    config.set(value, path);
  }

  @Override
  public boolean contains(String... path) {
    return config.contains(path);
  }

  @Override
  public String getName() {
    return config.getName();
  }

  @Override
  public Map<String[], Object> getValues(boolean deep, String... path) {
    return config.getValues(deep, path);
  }

  @Override
  public void setup() {
    config.setup();
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

  @Override
  public void remove(String... path) {
    config.remove(path);
  }

  @Override
  public void clear() {
    config.clear();
  }

  @Override
  public Object get(String... path) {
    return config.get(path);
  }

  @Override
  public Object getNormalized(Object def, String... path) {
    return config.getNormalized(def, path);
  }

  @Override
  public Object getNormalized(String... path) {
    return config.getNormalized(path);
  }

  @Override
  public <T> T getInstance(Class<T> type, T def, String... path) {
    return config.getInstance(type, def, path);
  }

  @Override
  public <T> T getInstance(Class<T> type, String... path) {
    return config.getInstance(type, path);
  }

  @Override
  public boolean isInstance(Class<?> type, String... path) {
    return config.isInstance(type, path);
  }

  @Override
  public Set<String[]> getKeys(boolean deep, String... path) {
    return config.getKeys(deep, path);
  }

  @Override
  public Map<String[], Object> getNormalizedValues(boolean deep, String... path) {
    return config.getNormalizedValues(deep, path);
  }

  @Override
  public Object normalizeObject(Object object) {
    return config.normalizeObject(object);
  }

  @Override
  public void setIfAbsent(Object value, String... path) {
    config.setIfAbsent(value, path);
  }

  @Override
  public void setIfAbsent(Map<String[], Object> map) {
    config.setIfAbsent(map);
  }

  @Override
  public List<String> getComment(CommentType type, String... path) {
    return config.getComment(type, path);
  }

  @Override
  public void setComment(CommentType type, List<String> value, String... path) {
    config.setComment(type, value, path);
  }

  @Override
  public List<String> getComment(String... path) {
    return config.getComment(path);
  }

  @Override
  public void setComment(List<String> value, String... path) {
    config.setComment(value, path);
  }
}
