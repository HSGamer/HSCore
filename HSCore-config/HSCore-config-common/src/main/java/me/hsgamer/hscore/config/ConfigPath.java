package me.hsgamer.hscore.config;

public interface ConfigPath<T> {

  /**
   * Set the value
   *
   * @param value the value
   */
  void setValue(T value);

  /**
   * Get the path to the value
   *
   * @return the path
   */
  String getPath();

  /**
   * Get the config
   *
   * @return the config
   */
  Config getConfig();

  /**
   * Set the config.
   *
   * @param config the config
   */
  void setConfig(Config config);

}
