package me.hsgamer.hscore.config.path;

import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.PathString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * A simple config path
 *
 * @param <T> the type of the value
 */
public class BaseConfigPath<T> implements ConfigPath<T> {

  private final Function<Object, T> typeConverter;
  private final PathString path;
  private final T def;
  private Config config;

  /**
   * Create a config path
   *
   * @param path          the path to the value
   * @param def           the default value if it's not found
   * @param typeConverter how to convert the raw object to the needed type of value
   */
  public BaseConfigPath(@NotNull final PathString path, @Nullable final T def, @NotNull final Function<Object, T> typeConverter) {
    this.path = path;
    this.def = def;
    this.typeConverter = typeConverter;
  }

  @Override
  public T getValue() {
    return config == null ? def : getValue(config);
  }

  @Override
  public void setValue(@Nullable final T value) {
    if (config != null) {
      setValue(value, config);
    }
  }

  @Override
  public T getValue(@NotNull Config config) {
    Object rawValue = config.getNormalized(path, def);
    return rawValue == null ? def : typeConverter.apply(rawValue);
  }

  @Override
  public void setValue(@Nullable T value, @NotNull Config config) {
    config.set(path, value);
  }

  @Override
  @NotNull
  public PathString getPath() {
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
    this.migrateConfig(config);
  }

  @Override
  public void migrateConfig(@NotNull final Config config) {
    config.setIfAbsent(path, def);
  }
}
