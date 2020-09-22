package me.hsgamer.hscore.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface of ConfigPath classes
 *
 * @param <T> the type of the value
 */
public interface ConfigPath<T> {

  /**
   * Get the value
   *
   * @return the value
   */
  @Nullable
  T getValue();

  /**
   * Set the value
   *
   * @param value the value
   */
  void setValue(@Nullable final T value);

  /**
   * Get the path to the value
   *
   * @return the path
   */
  @NotNull
  String getPath();

  /**
   * Get the config
   *
   * @return the config
   */
  @Nullable
  Config getConfig();

  /**
   * Set the config.
   *
   * @param config the config
   */
  void setConfig(@NotNull final Config config);
}
