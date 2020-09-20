package me.hsgamer.hscore.config;

/**
 * The base of ConfigPath classes
 *
 * @param <T> the type of the value
 */
public abstract class BaseConfigPath<T> implements ConfigPath<T> {

  protected final T def;

  protected final String path;

  protected Config config;

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   */
  protected BaseConfigPath(final String path, final T def) {
    this.path = path;
    this.def = def;
  }

  /**
   * Get the value
   *
   * @return the value
   */
  public abstract T getValue();

  /**
   * Set the value
   *
   * @param value the value
   */
  @Override
  public void setValue(final T value) {
    if (this.config == null) {
      return;
    }
    this.config.getConfig().set(this.path, value);
  }

  /**
   * Get the path to the value
   *
   * @return the path
   */
  @Override
  public final String getPath() {
    return this.path;
  }

  /**
   * Get the config
   *
   * @return the config
   */
  @Override
  public final Config getConfig() {
    return this.config;
  }

  /**
   * Set the config.
   *
   * @param config the config
   */
  @Override
  public void setConfig(final Config config) {
    this.config = config;
  }

}
