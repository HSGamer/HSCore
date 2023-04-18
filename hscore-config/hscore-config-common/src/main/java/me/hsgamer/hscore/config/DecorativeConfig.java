package me.hsgamer.hscore.config;

import java.util.Map;

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
    return this.config.getOriginal();
  }

  @Override
  public Object get(PathString path, Object def) {
    return this.config.get(path, def);
  }

  @Override
  public void set(PathString path, Object value) {
    this.config.set(path, value);
  }

  @Override
  public boolean contains(PathString path) {
    return this.config.contains(path);
  }

  @Override
  public String getName() {
    return this.config.getName();
  }

  @Override
  public void addDefault(PathString path, Object value) {
    this.config.addDefault(path, value);
  }

  @Override
  public Map<PathString, Object> getValues(PathString path, boolean deep) {
    return this.config.getValues(path, deep);
  }

  @Override
  public void setup() {
    this.config.setup();
  }

  @Override
  public void save() {
    this.config.save();
  }

  @Override
  public void reload() {
    this.config.reload();
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
  public String getComment(PathString path, CommentType type) {
    return this.config.getComment(path, type);
  }

  @Override
  public void setComment(PathString path, String value, CommentType type) {
    this.config.setComment(path, value, type);
  }
}
