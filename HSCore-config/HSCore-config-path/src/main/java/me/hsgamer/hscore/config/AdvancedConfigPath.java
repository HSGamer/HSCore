package me.hsgamer.hscore.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An advanced config path
 *
 * @param <F> the type of the raw value from the config
 * @param <T> the type of the final value
 */
public abstract class AdvancedConfigPath<F, T> implements ConfigPath<T> {

  private final String path;
  private final T def;
  private Config config;

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public AdvancedConfigPath(@NotNull final String path, @Nullable final T def) {
    this.path = path;
    this.def = def;
  }

  /**
   * Get the raw value from the config
   *
   * @param config the config
   *
   * @return the raw value
   */
  @Nullable
  public abstract F getFromConfig(@NotNull final Config config);

  /**
   * Convert to the final value
   *
   * @param rawValue the raw value
   *
   * @return the final value
   */
  @Nullable
  public abstract T convert(@NotNull final F rawValue);

  /**
   * Convert to the raw value
   *
   * @param value the value
   *
   * @return the raw value
   */
  @Nullable
  public abstract F convertToRaw(@NotNull final T value);

  @Override
  @Nullable
  public T getValue() {
    if (config == null) {
      return def;
    }

    F rawValue = getFromConfig(config);
    if (rawValue == null) {
      return def;
    }

    T value = convert(rawValue);
    return value != null ? value : def;
  }

  @Override
  public void setValue(@Nullable final T value) {
    if (config == null) {
      return;
    }

    config.getConfig().set(path, value != null ? convertToRaw(value) : null);
  }

  @Override
  @NotNull
  public String getPath() {
    return path;
  }

  @Override
  @Nullable
  public Config getConfig() {
    return config;
  }

  @Override
  public void setConfig(@NotNull final Config config) {
    this.config = config;
    config.getConfig().addDefault(path, def != null ? convertToRaw(def) : null);
  }
}
