package me.hsgamer.hscore.config;

import java.util.function.Function;

/**
 * A simple config path
 *
 * @param <T> the type of the value
 */
public abstract class DefaultPath<T> extends BaseConfigPath<T> {

  private final Function<Object, T> typeConverter;

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def the default value if it's not found
   * @param typeConverter how to convert the raw object to the needed type of value
   */
  protected DefaultPath(final String path, final T def, final Function<Object, T> typeConverter) {
    super(path, def);
    this.typeConverter = typeConverter;
  }

  @Override
  public final T getValue() {
    if (this.config == null) {
      return this.def;
    }

    final Object rawValue = this.config.get(this.path, this.def);
    if (rawValue == null) {
      return this.def;
    }

    return this.typeConverter.apply(rawValue);
  }

  @Override
  public final void setConfig(final Config config) {
    super.setConfig(config);
    config.getConfig().addDefault(this.path, this.def);
  }

}
