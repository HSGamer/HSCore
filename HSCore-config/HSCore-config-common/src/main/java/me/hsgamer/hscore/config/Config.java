package me.hsgamer.hscore.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.FileConfiguration;

public interface Config {

  /**
   * Set up the config
   */
  void setupConfig();

  /**
   * Reload the config
   */
  void reloadConfig();

  /**
   * Save the config
   */
  void saveConfig();

  /**
   * Get the instance of the config file
   *
   * @return the config
   */
  @NotNull
  FileConfiguration getConfig();

  /**
   * Get the value from the config
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   * @return the value
   */
  @Nullable
  Object get(@NotNull final String path, @Nullable final Object def);

  /**
   * Get the value from the config
   *
   * @param path the path to the value
   * @return the value
   */
  @Nullable
  default Object get(@NotNull final String path) {
    return get(path, null);
  }
}
