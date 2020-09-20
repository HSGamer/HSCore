package me.hsgamer.hscore.config;

/**
 * An advanced config path
 *
 * @param <F> the type of the raw value from the config
 * @param <T> the type of the final value
 */
public abstract class AdvancedConfigPath<F, T> extends BaseConfigPath<T> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   */
  protected AdvancedConfigPath(final String path, final T def) {
    super(path, def);
  }

  /**
   * Get the raw value from the config
   *
   * @param config the config
   * @return the raw value
   */
  public abstract F getFromConfig(Config config);

  /**
   * Convert to the final value
   *
   * @param rawValue the raw value
   * @return the final value
   */
  public abstract T convert(F rawValue);

  /**
   * Convert to the raw value
   *
   * @param value the value
   * @return the raw value
   */
  public abstract F convertToRaw(T value);

  @Override
  public final T getValue() {
    if (this.config == null) {
      return this.def;
    }

    final F rawValue = this.getFromConfig(this.config);
    if (rawValue == null) {
      return this.def;
    }

    final T value = this.convert(rawValue);
    if (value != null) {
      return value;
    }
    return this.def;
  }

  @Override
  public final void setValue(final T value) {
    if (this.config == null) {
      return;
    }
    this.config.getConfig().set(this.path, this.convertToRaw(value));
  }

  @Override
  public final void setConfig(final Config config) {
    super.setConfig(config);
    config.getConfig().addDefault(this.path, this.convertToRaw(this.def));
  }

}
