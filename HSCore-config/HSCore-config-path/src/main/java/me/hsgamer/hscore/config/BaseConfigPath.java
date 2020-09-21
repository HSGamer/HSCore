package me.hsgamer.hscore.config;

/**
 * The base of ConfigPath classes
 *
 * @param <T> the type of the value
 */
public abstract class BaseConfigPath<T> {

  protected final T def;
  protected final String path;
  protected Config config;

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public BaseConfigPath(String path, T def) {
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
  public void setValue(T value) {
    if (config == null) {
      return;
    }

    config.getConfig().set(path, value);
  }

  /**
   * Get the path to the value
   *
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * Get the config
   *
   * @return the config
   */
  public Config getConfig() {
    return config;
  }

  /**
   * Set the config.
   *
   * @param config the config
   */
  public void setConfig(Config config) {
    this.config = config;
  }
}
