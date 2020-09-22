package me.hsgamer.hscore.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * A simple config path
 *
 * @param <T> the type of the value
 */
public class ConfigPath<T> implements BaseConfigPath<T> {

  private final Function<Object, T> typeConverter;
  private final String path;
  private final T def;
  private Config config;

  /**
   * Create a config path
   *
   * @param path          the path to the value
   * @param def           the default value if it's not found
   * @param typeConverter how to convert the raw object to the needed type of value
   */
  public ConfigPath(@NotNull final String path, @Nullable final T def, @NotNull final Function<Object, T> typeConverter) {
    this.path = path;
    this.def = def;
    this.typeConverter = typeConverter;
  }

  @Override
  @Nullable
  public final T getValue() {
    if (config == null) {
      return def;
    }

    Object rawValue = config.get(path, def);
    if (rawValue == null) {
      return def;
    }

    return typeConverter.apply(rawValue);
  }

  @Override
  public void setValue(@Nullable final T value) {
    if (config == null) {
      return;
    }

    config.getConfig().set(path, value);
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
    config.getConfig().addDefault(path, def);
  }
}
