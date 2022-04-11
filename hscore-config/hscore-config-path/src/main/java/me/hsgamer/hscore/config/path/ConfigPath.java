package me.hsgamer.hscore.config.path;

import me.hsgamer.hscore.config.Config;
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
  T getValue();

  /**
   * Set the value
   *
   * @param value the value
   */
  void setValue(@Nullable final T value);

  /**
   * Get the value from the config.
   * This is used to get the value from multiple configs.
   *
   * @param config the config
   *
   * @return the value
   */
  T getValue(@NotNull final Config config);

  /**
   * Set the value to the config.
   * This is used to set the value to multiple configs.
   *
   * @param value  the value
   * @param config the config
   */
  void setValue(@Nullable final T value, @NotNull final Config config);

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

  /**
   * Manually update the config (Mainly used when updating new settings)
   *
   * @param config the config
   */
  default void migrateConfig(@NotNull final Config config) {
    // EMPTY
  }

  /**
   * Reload the path
   */
  default void reload() {
    // EMPTY
  }

  /**
   * Set the value and save the config
   *
   * @param value  the value
   * @param config the config
   */
  default void setAndSave(@Nullable final T value, @NotNull final Config config) {
    setValue(value);
    config.save();
  }

  /**
   * Set the value and save the config
   *
   * @param value the value
   */
  default void setAndSave(@Nullable final T value) {
    setValue(value);
    Config config = getConfig();
    if (config != null) {
      config.save();
    }
  }
}
